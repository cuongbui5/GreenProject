package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateVariationOptionRequest;
import com.example.greenproject.dto.req.CreateVariationRequest;
import com.example.greenproject.dto.res.BaseResponse;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.model.Variation;
import com.example.greenproject.service.VariationOptionService;
import com.example.greenproject.service.VariationService;
import com.example.greenproject.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/variation_options")
@RequiredArgsConstructor
public class VariationOptionController {
    private final VariationOptionService variationOptionService;
    @PostMapping("/create")
    public ResponseEntity<?> createVariationOption(@RequestBody CreateVariationOptionRequest createVariationOptionRequest){
        variationOptionService.createVariationOption(createVariationOptionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse(HttpStatus.CREATED.value(), Constants.SUCCESS_MESSAGE));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVariationOption(@PathVariable Long id){
        variationOptionService.deleteVariation(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse(HttpStatus.CREATED.value(), Constants.SUCCESS_MESSAGE));
    }



}
