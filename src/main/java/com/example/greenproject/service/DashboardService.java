package com.example.greenproject.service;

import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.model.Product;
import com.example.greenproject.model.User;
import com.example.greenproject.model.enums.OrderStatus;
import com.example.greenproject.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.AbstractMap;

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

    public long getUsersByQuarter(int quarter, int year) {
        ZonedDateTime now = ZonedDateTime.now();
        if(quarter == 0 && year == 0){
             year = now.getYear();
             quarter = now.getMonthValue();
        }

        return userRepository.countUsersByQuarter(year, quarter);
    }

    public Object getTopUsersByTotalOrderValue(int quarter,int year,int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        AbstractMap.SimpleEntry<ZonedDateTime, ZonedDateTime> dateRange = getQuarterDateRange(quarter, year);
        ZonedDateTime startDate = dateRange.getKey();
        ZonedDateTime endDate = dateRange.getValue();

        Page<User> results = userRepository.findByTotalOrderValue(startDate,endDate,pageable);
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
        Page<Product> products = productRepository.findTopSellingProductByQuarter(year,quarter,pageable);

        return new PaginatedResponse<>(
                products.getContent().stream().map(Product::mapToProductDto).toList(),
                products.getTotalPages(),
                products.getNumber()+1,
                products.getTotalElements()
        );
    }


    /*--------------Thống kê order -------------*/
    public long countByIsPaidTrue(){
        return orderRepository.countByIsPaidTrue();
    }

    public double getRevenue(int quarter,int year){
        AbstractMap.SimpleEntry<ZonedDateTime, ZonedDateTime> dateRange = getQuarterDateRange(quarter, year);
        ZonedDateTime startDate = dateRange.getKey();
        ZonedDateTime endDate = dateRange.getValue();

        Double totalRevenue = orderRepository.calculateTotalRevenue(startDate,endDate);
        if(totalRevenue == null){
            throw new RuntimeException("Ko co thong tin doanh thu cua quy " + quarter + " nam " + year);
        }
        return totalRevenue;
    }

    public int getTotalOrderByStatus(OrderStatus orderStatus, int quarter, int year){
        AbstractMap.SimpleEntry<ZonedDateTime, ZonedDateTime> dateRange = getQuarterDateRange(quarter, year);
        ZonedDateTime startDate = dateRange.getKey();
        ZonedDateTime endDate = dateRange.getValue();
        return orderRepository.countByStatusAndDateRange(orderStatus, startDate, endDate);

    }

    public Double calcPercentageReturnedOrder(int quarter,int year){

        AbstractMap.SimpleEntry<ZonedDateTime, ZonedDateTime> dateRange = getQuarterDateRange(quarter, year);
        ZonedDateTime startDate = dateRange.getKey();
        ZonedDateTime endDate = dateRange.getValue();

        int totalReturnedOrder = orderRepository.countByStatusAndDateRange(OrderStatus.RETURNED, startDate, endDate);
        double totalOrderValue  = orderRepository.calculateTotalRevenue(startDate,endDate);
        return totalReturnedOrder / totalOrderValue * 100;
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

    /*-----------------Thống kê voucher--------------------*/
    public long getTotalVoucher() {
        return voucherRepository.count();
    }

}
