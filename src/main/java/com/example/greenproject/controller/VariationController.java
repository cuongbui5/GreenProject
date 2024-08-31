package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateVariationRequest;
import com.example.greenproject.dto.req.UpdateVariationRequest;
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
        return ResponseEntity.status(HttpStatus.OK).body(variations);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getVariationByCategoryId(@PathVariable("id") Long id){
        List<Variation> variations = variationService.getVariationByCategoryId(id);
        return ResponseEntity.status(HttpStatus.OK).body(variations);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createVariation(@RequestBody CreateVariationRequest createVariationRequest){
        Variation variation = variationService.createVariation(createVariationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(variation);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVariation(@PathVariable("id") Long variationId,@RequestBody UpdateVariationRequest updateVariationRequest){
        Variation updateVariation = variationService.updateVariation(variationId,updateVariationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updateVariation);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVariation(@PathVariable("id") Long id){
        //variationService.deleteVariation(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete success variation id " + id);
    }
}
