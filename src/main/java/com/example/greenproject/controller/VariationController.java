package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateVariationRequest;
import com.example.greenproject.dto.req.UpdateVariationRequest;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.model.Variation;
import com.example.greenproject.service.VariationService;
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

    @GetMapping
    public ResponseEntity<?> getAllVariation(){
        List<Variation> variations = variationService.getAllVariation();
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Successfully retrieved variation list ",
                variations));
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getVariationByCategoryId(@PathVariable("id") Long id){
        List<Variation> variations = variationService.getVariationByCategoryId(id);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Successfully retrieved variation by category id " + id,
                variations));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createVariation(@RequestBody CreateVariationRequest createVariationRequest){
        Variation variation = variationService.createVariation(createVariationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse(
                HttpStatus.CREATED.value(),
                "Successfully create new variation",
                variation));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVariationById(@PathVariable("id") Long variationId,@RequestBody UpdateVariationRequest updateVariationRequest){
        Variation updateVariation = variationService.updateVariationById(variationId,updateVariationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Successfully update variation",
                updateVariation));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVariationById(@PathVariable("id") Long id){
        variationService.deleteVariationById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Successfully delete variation id " + id,
                null));
    }

    @PostMapping("/{variationId}/remove/categories/{categoryId}")
    public ResponseEntity<?> removeCategoryFromVariation(@PathVariable("variationId") Long variationId,@PathVariable("categoryId") Long categoryId){
        variationService.removeCategoryFromVariation(variationId,categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Successfully remove variation id " + variationId +" from category id " + categoryId,
                null));
    }

    @PostMapping("/{variationId}/add/categories/{categoryId}")
    public ResponseEntity<?> addCategoryToVariation(@PathVariable("variationId") Long variationId,@PathVariable("categoryId") Long categoryId){
        Variation addVariation = variationService.addCategoryToVariation(variationId,categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Successfully add variation id " + variationId +" to category id " + categoryId,
                addVariation));
    }
}
