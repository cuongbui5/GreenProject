package com.example.greenproject.controller;

import com.example.greenproject.dto.req.VoucherRequest;
import com.example.greenproject.dto.res.BaseResponse;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.dto.res.VoucherDto;
import com.example.greenproject.model.Voucher;
import com.example.greenproject.service.VoucherService;
import com.example.greenproject.utils.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;

    @GetMapping
    public ResponseEntity<?> getAllVouchers(@RequestParam(value = "pageNum",required = false) Integer pageNum,
                                            @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                            @RequestParam(value = "search",required = false) String search){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        voucherService.getAllVouchers(pageNum,pageSize,search)
                ));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createVoucher(@Valid @RequestBody VoucherRequest voucherRequest){
        System.out.println("createVoucher");
        System.out.println(voucherRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new DataResponse(
                        HttpStatus.CREATED.value(),
                        Constants.SUCCESS_MESSAGE,
                        voucherService.createVoucher(voucherRequest)
                ));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVoucher(@PathVariable("id") Long id,@Valid @RequestBody VoucherRequest voucherRequest){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        voucherService.updateVoucher(id,voucherRequest)
                ));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVoucher(@PathVariable("id") Long id){
        voucherService.deleteVoucher(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE
                ));
    }

    @GetMapping("/valid")
    public ResponseEntity<DataResponse> getValidVouchers(
            @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        voucherService.getValidVouchers(pageNum, pageSize)
                ));
    }

    @GetMapping("/my-voucher")
    public ResponseEntity<?> getVouchersByUserId(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        voucherService.getVouchersByUserId(pageNum, pageSize)
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
                        voucherService.redeemVoucher(voucherId)
                )
        );
    }
}
