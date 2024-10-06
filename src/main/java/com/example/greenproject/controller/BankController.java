package com.example.greenproject.controller;

import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.service.BankService;
import com.example.greenproject.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/banks")
@RequiredArgsConstructor
public class BankController {
    private final BankService bankService;

    @GetMapping
    public ResponseEntity<?> getAllBanks() {
        return ResponseEntity.ok().body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                bankService.findAll()

        ));
    }
}
