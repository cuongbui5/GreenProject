package com.example.greenproject.service;

import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.model.User;
import com.example.greenproject.model.enums.OrderStatus;
import com.example.greenproject.repository.OrderRepository;
import com.example.greenproject.repository.ProductRepository;
import com.example.greenproject.repository.UserRepository;
import com.example.greenproject.repository.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final VoucherRepository voucherRepository;


    /*--------------Thống kê user-------------*/
    public long getTotalUser(){
        return userRepository.count();
    }

    public Map<String,Object> getUsersByQuarter(int quarter, int year) {
        ZonedDateTime now = ZonedDateTime.now();
        if(quarter == 0 && year == 0){
             year = now.getYear();
             quarter = now.getMonthValue();
        }

         long currentUsersCount = userRepository.countUsersByQuarter(year, quarter);

        // Lấy doanh thu của quý trước đó
        int previousQuarter = quarter - 1;
        int previousYear = year;

        if (previousQuarter == 0) {
            previousQuarter = 4;
            previousYear = year - 1;
        }

        long previousUsersCount = userRepository.countUsersByQuarter(previousYear, previousQuarter);

        // Nếu không có dữ liệu của quý trước, tỷ lệ tăng trưởng là 0
        double growthPercentage = 0.0;
        if (previousUsersCount > 0) {
            growthPercentage = ((double) (currentUsersCount - previousUsersCount) / previousUsersCount) * 100;
        }

        // Tạo map chứa dữ liệu trả về
        Map<String, Object> revenueData = new HashMap<>();
        revenueData.put("users", currentUsersCount);
        revenueData.put("growth", growthPercentage);

        return revenueData;
    }

    public Object getTopUsersByTotalOrderValue(int quarter,int year,int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        AbstractMap.SimpleEntry<ZonedDateTime, ZonedDateTime> dateRange = getQuarterDateRange(quarter, year);
        ZonedDateTime startDate = dateRange.getKey();
        ZonedDateTime endDate = dateRange.getValue();

        Page<User> results = userRepository.findByTotalOrderValue(startDate,endDate, OrderStatus.DELIVERED,pageable);
        return new PaginatedResponse<>(
                results.getContent().stream().map(User::mapToUserDtoLazy).toList(),
                results.getTotalPages(),
                results.getNumber()+1,
                results.getTotalElements()
        );
    }


    /*-----------------Thống kê sản phẩm--------------------*/
    public long getTotalProduct(){
        return productRepository.count();
    }

    @Transactional
    public Object getTopSoldProductByQuarter(int quarter, int year,int pageNum, int pageSize){
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        Page<Object> results = productRepository.findTopSellingProductByQuarter(year,quarter,pageable);

        List<Map<String,Object>> finalResults = new ArrayList<>(); // id va ket qua cua (total, product)

        for (Object result : results) {
            Map<String,Object> productDtoMap = new HashMap<>(); // total, product

            Object[] row = (Object[]) result;
            Long productId = (Long) row[0];
            String productName = (String) row[1];
            Double minPrice = (Double) row[2];
            Double maxPrice = (Double) row[3];
            Long totalSold = (Long) row[4];
            Long totalRating = (Long) row[5];
            Long countReviews = (Long) row[6];

            productDtoMap.put("id",productId);
            productDtoMap.put("name",productName);
            productDtoMap.put("min_price",minPrice);
            productDtoMap.put("max_price",maxPrice);
            productDtoMap.put("total_sold",totalSold);
            productDtoMap.put("total_rating",totalRating);
            productDtoMap.put("count_reviews",countReviews);
            finalResults.add(productDtoMap);
        }

        return finalResults;
    }



    /*------------------------------------------Da fix-------------------------------------------------*/
    /*------------------------------------------Da fix-------------------------------------------------*/
    public Map<String,Object> getRevenue(int quarter,int year){
        ZonedDateTime now = ZonedDateTime.now();
        if(quarter == 0 && year == 0){
            year = now.getYear();
            quarter = now.getMonthValue();
        }

        AbstractMap.SimpleEntry<ZonedDateTime, ZonedDateTime> dateRange = getQuarterDateRange(quarter, year);
        ZonedDateTime startDate = dateRange.getKey();
        ZonedDateTime endDate = dateRange.getValue();

        Double currentRevenue = orderRepository.calculateTotalRevenue(startDate,endDate,OrderStatus.DELIVERED);
        if (currentRevenue == null) {
            throw new RuntimeException("Không có thông tin doanh thu của quý " + quarter + " năm " + year);
        }

        // Lấy doanh thu của quý trước đó
        int previousQuarter = quarter - 1;
        int previousYear = year;

        if (previousQuarter == 0) {
            previousQuarter = 4;
            previousYear = year - 1;
        }

        AbstractMap.SimpleEntry<ZonedDateTime, ZonedDateTime> previousDateRange = getQuarterDateRange(previousQuarter, previousYear);
        ZonedDateTime previousStartDate = previousDateRange.getKey();
        ZonedDateTime previousEndDate = previousDateRange.getValue();
        Double previousRevenue = orderRepository.calculateTotalRevenue(previousStartDate, previousEndDate,OrderStatus.DELIVERED);

        // Nếu không có dữ liệu của quý trước, tỷ lệ tăng trưởng là 0
        double growthPercentage = 0.0;
        if (previousRevenue != null && previousRevenue > 0) {
            growthPercentage = ((currentRevenue - previousRevenue) / previousRevenue) * 100;
        }

        // Tạo map chứa dữ liệu trả về
        Map<String, Object> revenueData = new HashMap<>();
        revenueData.put("revenue", currentRevenue);
        revenueData.put("growth", growthPercentage);

        return revenueData;
    }

    /*--------------Thống kê order -------------*/
    public long countByIsPaidTrue(){
        return orderRepository.countByIsPaidTrue();
    }


    private int getTotalOrderByStatus(OrderStatus orderStatus, int quarter, int year){
        AbstractMap.SimpleEntry<ZonedDateTime, ZonedDateTime> dateRange = getQuarterDateRange(quarter, year);
        ZonedDateTime startDate = dateRange.getKey();
        ZonedDateTime endDate = dateRange.getValue();
        return orderRepository.countByStatusAndDateRange(orderStatus, startDate, endDate);
    }


    public Map<String, Object> getOrderStatusData(int quarter, int year) {
        int deliveredOrders = getTotalOrderByStatus(OrderStatus.DELIVERED, quarter, year);
        int pendingOrders = getTotalOrderByStatus(OrderStatus.PENDING, quarter, year);
        int shippedOrders = getTotalOrderByStatus(OrderStatus.SHIPPED, quarter, year);
        int processingOrders = getTotalOrderByStatus(OrderStatus.PROCESSING, quarter, year);
        int canceledOrders = getTotalOrderByStatus(OrderStatus.CANCELED, quarter, year);
        int returnedOrders = getTotalOrderByStatus(OrderStatus.RETURNED, quarter, year);

        // Tính tổng số đơn hàng
        int totalOrders = deliveredOrders + pendingOrders + shippedOrders + processingOrders +canceledOrders + returnedOrders;

        // Tính tỷ lệ phần trăm cho từng trạng thái
        double deliveredPercentage = (double) deliveredOrders / totalOrders * 100;
        double pendingPercentage = (double) pendingOrders / totalOrders * 100;
        double shippedPercentage= (double) shippedOrders / totalOrders * 100;
        double processingPercentage = (double) processingOrders / totalOrders * 100;
        double canceledPercentage = (double) canceledOrders / totalOrders * 100;
        double returnedPercentage = (double) returnedOrders / totalOrders * 100;

        Map<String, Object> statusData = new HashMap<>();

        List<Map<String, Object>> statusList = new ArrayList<>();
        // Thêm từng trạng thái vào danh sách
        statusList.add(Map.of(
                "status", "Delivered",
                "count", deliveredOrders,
                "percentage", String.format("%.1f%%", deliveredPercentage)
        ));
        statusList.add(Map.of(
                "status", "Pending",
                "count", pendingOrders,
                "percentage", String.format("%.1f%%", pendingPercentage)
        ));
        statusList.add(Map.of(
                "status", "Shipped",
                "count", shippedOrders,
                "percentage", String.format("%.1f%%", shippedPercentage)
        ));
        statusList.add(Map.of(
                "status", "Processing",
                "count", processingOrders,
                "percentage", String.format("%.1f%%", processingPercentage)
        ));
        statusList.add(Map.of(
                "status", "Canceled",
                "count", canceledOrders,
                "percentage", String.format("%.1f%%", canceledPercentage)
        ));
        statusList.add(Map.of(
                "status", "Returned",
                "count", returnedOrders,
                "percentage", String.format("%.1f%%", returnedPercentage)
        ));

        // Thêm danh sách trạng thái vào dữ liệu JSON
        statusData.put("statuses", statusList);
        return statusData;
    }


    /*-----------------Thống kê voucher--------------------*/
    public long getTotalVoucher() {
        return voucherRepository.count();
    }

    private AbstractMap.SimpleEntry<ZonedDateTime, ZonedDateTime> getQuarterDateRange(int quarter, int year) {
        ZonedDateTime startDate;
        ZonedDateTime endDate;

        if (quarter == 0 && year == 0) {
            int month = LocalDateTime.now().getMonth().getValue();
            year = LocalDateTime.now().getYear();
            quarter = (month - 1) / 3 + 1; // Tính quarter dựa trên tháng
        }

        ZoneId zoneId = ZoneId.of("UTC");

        switch (quarter) {
            case 1:
                startDate = ZonedDateTime.of(year, 1, 1, 0, 0, 0, 0, zoneId);
                endDate = ZonedDateTime.of(year, 3, 31, 23, 59, 59, 999999999, zoneId);
                break;
            case 2:
                startDate = ZonedDateTime.of(year, 4, 1, 0, 0, 0, 0, zoneId);
                endDate = ZonedDateTime.of(year, 6, 30, 23, 59, 59, 999999999, zoneId);
                break;
            case 3:
                startDate = ZonedDateTime.of(year, 7, 1, 0, 0, 0, 0, zoneId);
                endDate = ZonedDateTime.of(year, 9, 30, 23, 59, 59, 999999999, zoneId);
                break;
            case 4:
                startDate = ZonedDateTime.of(year, 10, 1, 0, 0, 0, 0, zoneId);
                endDate = ZonedDateTime.of(year, 12, 31, 23, 59, 59, 999999999, zoneId);
                break;
            default:
                throw new IllegalArgumentException("Invalid quarter: " + quarter);
        }

        return new AbstractMap.SimpleEntry<>(startDate, endDate);
    }
}
