package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateVariationOptionRequest;
import com.example.greenproject.dto.req.UpdateVariationOptionRequest;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.model.VariationOption;
import com.example.greenproject.service.VariationOptionService;
import com.example.greenproject.service.VariationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/variation_options")
@RequiredArgsConstructor
public class VariationOptionController {
    private final VariationOptionService variationOptionService;

    @GetMapping("/variations/{id}")
    public ResponseEntity<?> getAllVariationOptionByVariationId(@PathVariable("id") Long variationId){
        List<VariationOption> variationOptions = variationOptionService.getVariationOptionByVariationId(variationId);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Successful retrieved variation option list by variation id " + variationId,
                variationOptions
        ));
    }

    @PostMapping("/create/variations/{id}")
    public ResponseEntity<?> createVariationOption(@PathVariable("id") Long variationId, @RequestBody CreateVariationOptionRequest createVariationOptionRequest){
        VariationOption saveVariationOption = variationOptionService.createVariationOption(variationId,createVariationOptionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse(
                HttpStatus.CREATED.value(),
                "Successful create and add new variation option to variation" + variationId,
                saveVariationOption
        ));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateVariationOption(@PathVariable("id") Long variationOptionId, @RequestBody UpdateVariationOptionRequest updateVariationOptionRequest){
        VariationOption saveVariationOption = variationOptionService.updateVariationOption(variationOptionId,updateVariationOptionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse(
                HttpStatus.CREATED.value(),
                "Successful create and add new variation option to variation" + variationOptionId,
                saveVariationOption
        ));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVariationOption(@PathVariable("id") Long variationOptionId){
        variationOptionService.deleteVariation(variationOptionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse(
                HttpStatus.CREATED.value(),
                "Successful create and add new variation option to variation" + variationOptionId,
                null
        ));
    }
}
