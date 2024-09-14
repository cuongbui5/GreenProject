package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateVariationRequest;
import com.example.greenproject.dto.req.UpdateVariationRequest;
import com.example.greenproject.dto.res.BaseResponse;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.dto.res.VariationDto;
import com.example.greenproject.model.Variation;
import com.example.greenproject.service.VariationService;
import com.example.greenproject.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/variations")
@RequiredArgsConstructor
public class VariationController {
    private final VariationService variationService;

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getAllVariationByCategoryId(@PathVariable Long categoryId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        variationService.getAllVariationByCategoryId(categoryId)
                ));
    }

    @GetMapping
    public ResponseEntity<?> getAllVariations(@RequestParam(value = "pageNum",required = false) Integer pageNum,
                                              @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                              @RequestParam(value = "search",required = false) String search){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        variationService.getAllVariations(pageNum,pageSize,search)));
    }



    @PostMapping("/create")
    public ResponseEntity<?> createVariation(@RequestBody CreateVariationRequest createVariationRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DataResponse(
                        HttpStatus.CREATED.value(),
                        Constants.SUCCESS_MESSAGE,
                        variationService.createVariation(createVariationRequest)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVariation(@PathVariable("id") Long variationId,@RequestBody UpdateVariationRequest updateVariationRequest){

        return ResponseEntity.status(HttpStatus.OK).
                body(new DataResponse(
                        HttpStatus.OK.value()
                        ,Constants.SUCCESS_MESSAGE
                        ,variationService.updateVariationById(variationId,updateVariationRequest)
                ));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVariation(@PathVariable("id") Long id){
        variationService.deleteVariation(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE
                ));
    }
}
