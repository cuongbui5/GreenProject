package com.example.greenproject.controller;


import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.dto.res.VoucherDto;
import com.example.greenproject.service.UserVoucherService;
import com.example.greenproject.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user_voucher")
public class UserVoucherController {

    private final UserVoucherService userVoucherService;

    @GetMapping("/my-voucher")
    public ResponseEntity<?> getVouchersByUserId(
                                                 @RequestParam(defaultValue = "1") int pageNum,
                                                 @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        userVoucherService.getVouchersByUserId(pageNum, pageSize)
                        )
        );
    }

    @PostMapping("/redeem")
    public ResponseEntity<?> redeemVoucher(
            @RequestParam Long voucherId) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        userVoucherService.redeemVoucher(voucherId)
                )
        );
    }
}