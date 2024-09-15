package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateVoucherRequest;
import com.example.greenproject.dto.req.FilteringVoucherRequest;
import com.example.greenproject.dto.req.UpdateVoucherRequest;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.model.Voucher;
import com.example.greenproject.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/vouchers")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;

    @GetMapping("/page={pageNum}")
    public ResponseEntity<?> getAllVouchers(@PathVariable("pageNum") int pageNum){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        "Successfully retrieved vouchers list",
                        voucherService.getAllVouchers(pageNum,10)
                ));
    }

    @GetMapping("/available/page={pageNum}")
    public ResponseEntity<?> getAllVouchersAvailable(@PathVariable("pageNum") int pageNum){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        "Successfully retrieved vouchers available",
                        voucherService.getAllVoucherAvailable(pageNum,10)
                ));
    }

    @GetMapping("/filtering/page={pageNum}")
    public ResponseEntity<?> getVoucherFilter(@PathVariable("pageNum") int pageNum,@RequestBody FilteringVoucherRequest filteringVoucherRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        "Successfully retrieved vouchers list",
                        voucherService.filteringVoucher(pageNum,10,filteringVoucherRequest)
                ));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createVoucher(@RequestBody CreateVoucherRequest createVoucherRequest){
        Voucher saveVoucher = voucherService.createVoucher(createVoucherRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new DataResponse(
                        HttpStatus.CREATED.value(),
                        "Successfully create voucher",
                        saveVoucher
                ));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVoucher(@PathVariable("id") Long id,@RequestBody UpdateVoucherRequest updateVoucherRequest){
        Voucher updateVoucher = voucherService.updateVoucher(id,updateVoucherRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        "Successfully update voucher id " + id,
                        updateVoucherRequest
                ));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVoucher(@PathVariable("id") Long id){
        voucherService.deleteVoucher(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        "Successfully delete voucher id " + id,
                        null
                ));
    }
}
