package com.example.greenproject.controller;

import com.example.greenproject.dto.req.ReviewRequest;
import com.example.greenproject.dto.res.BaseResponse;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.service.ReviewService;
import com.example.greenproject.utils.Constants;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<?> getReviewByProductId(@Param("productItemId") Long productItemId,
                                                  @Param("pageNum") Integer pageNum,
                                                  @Param("pageSize") Integer pageSize){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                       HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        reviewService.getReviewByProductId(productItemId,pageNum,pageSize)
                ));
    }

    @GetMapping("/product_item/{productItemId}")
    public ResponseEntity<?> getReviewById(@PathVariable("productItemId") Long productItemId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        reviewService.getReviewByReviewId(productItemId)
                ));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createReview(@RequestBody @Valid ReviewRequest reviewRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        reviewService.createReview(reviewRequest)
                ));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateReview(@RequestBody @Valid ReviewRequest reviewRequest){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        reviewService.updateReview(reviewRequest)
                ));
    }

    @DeleteMapping("/delete/product_item/{productItemId}")
    public ResponseEntity<?> deleteReview(@PathVariable("productItemId") Long productItemId){
        reviewService.deleteReview(productItemId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE
                ));
    }
}
