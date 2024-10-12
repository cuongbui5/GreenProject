package com.example.greenproject.controller;

import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.model.enums.OrderStatus;
import com.example.greenproject.service.DashboardService;
import com.example.greenproject.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class AdminController {
    private final DashboardService dashboardService;

    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics() {
        long paidOrdersCount = dashboardService.countByIsPaidTrue();
        long totalProducts = dashboardService.getTotalProduct();
        long totalUser = dashboardService.getTotalUser();
        long totalVoucher = dashboardService.getTotalVoucher();

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("paid_orders_count", paidOrdersCount);
        statistics.put("total_product",totalProducts);
        statistics.put("total_user",totalUser);
        statistics.put("total_voucher",totalVoucher);

        return ResponseEntity.status(HttpStatus.OK).body(
                new DataResponse(HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        statistics)
        );
    }



    @GetMapping("/order")
    public ResponseEntity<?> getOrderByOrderStatus(@RequestParam(value = "quarter",defaultValue = "0") Integer quarter,
                                                   @RequestParam(value = "year",defaultValue = "0") Integer year){
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("orders",dashboardService.getOrderStatusData(quarter,year));
        return ResponseEntity.status(HttpStatus.OK).body(
                new DataResponse(HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        statistics)
        );
    }

    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenueByQuarter(@RequestParam(value = "quarter",defaultValue = "0") Integer quarter,
                                                 @RequestParam(value = "year",defaultValue = "0") Integer year){
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("quarter",quarter);
        statistics.put("year",year);
        statistics.put("orders",dashboardService.getRevenue(quarter,year));
        return ResponseEntity.status(HttpStatus.OK).body(
                new DataResponse(HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        statistics)
        );
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestParam(value = "quarter",defaultValue = "0") Integer quarter,
                                     @RequestParam(value = "year",defaultValue = "0") Integer year){
        Map<String,Object> statistics = new HashMap<>();
        statistics.put("users",dashboardService.getUsersByQuarter(quarter,year));

        return ResponseEntity.status(HttpStatus.OK).body(
                new DataResponse(HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        statistics)
        );
    }

    @GetMapping("/top-user")
    public ResponseEntity<?> getTopUserOrderValue(@RequestParam(value = "quarter",defaultValue = "0") Integer quarter,
                                                  @RequestParam(value = "year",defaultValue = "0") Integer year,
                                                  @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                                  @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        Map<String,Object> statistics = new HashMap<>();
        statistics.put("quarter",quarter);
        statistics.put("year",year);
        statistics.put("users",dashboardService.getTopUsersByTotalOrderValue(quarter,year,pageNum,pageSize));

        return ResponseEntity.status(HttpStatus.OK).body(
                new DataResponse(HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        statistics)
        );
    }

    @GetMapping("/products")
    public ResponseEntity<?> getUser(@RequestParam(value = "quarter",defaultValue = "0") Integer quarter,
                                     @RequestParam(value = "year",defaultValue = "0") Integer year,
                                     @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        Map<String,Object> statistics = new HashMap<>();
        statistics.put("quarter",quarter);
        statistics.put("year",year);
        statistics.put("top_sold_products",dashboardService.getTopSoldProductByQuarter(quarter,year,pageNum,pageSize));
        return ResponseEntity.status(HttpStatus.OK).body(
                new DataResponse(HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        statistics)
        );
    }


}
