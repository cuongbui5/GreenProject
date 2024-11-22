package com.example.greenproject.controller;

import com.example.greenproject.dto.req.LinkPaymentAccount;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.service.PaymentAccountService;
import com.example.greenproject.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment_accounts")
@RequiredArgsConstructor
public class PaymentAccountController {
    private final PaymentAccountService paymentAccountService;

    @GetMapping("/user")
    public ResponseEntity<?> getAllByUser() {
        return ResponseEntity.ok().body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                paymentAccountService.findAllByUser()
        ));

    }

    @PostMapping("/link")
    public ResponseEntity<?> linkUserToPaymentAccount(@RequestBody LinkPaymentAccount linkPaymentAccount) {
        return ResponseEntity.ok().body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                paymentAccountService.linkUserToPaymentAccount(linkPaymentAccount)
        ));

    }
}
