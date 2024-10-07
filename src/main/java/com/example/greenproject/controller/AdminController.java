package com.example.greenproject.controller;

import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.model.enums.OrderStatus;
import com.example.greenproject.service.OrderService;
import com.example.greenproject.service.ProductService;
import com.example.greenproject.service.UserService;
import com.example.greenproject.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class AdminController {
    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics() {
        long paidOrdersCount = orderService.countByIsPaidTrue();
        long totalProducts = productService.getTotalProduct();
        long totalUser = userService.getTotalUser();
        Double totalPaidOrderValue = orderService.getTotalPaidOrderValue();
        long totalNewUserThisMonth = userService.getUsersByCurrentMonth();

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("paidOrdersCount", paidOrdersCount);
        statistics.put("totalProduct",totalProducts);
        statistics.put("totalUser",totalUser);
        statistics.put("totalPaidOrderValue",totalPaidOrderValue);
        statistics.put("totalNewUser",totalNewUserThisMonth);
        statistics.put("bestSellerProducts",productService.getProductsByTopSold(1,5));

        return ResponseEntity.status(HttpStatus.OK).body(
                new DataResponse(HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        statistics)
        );
    }
}
