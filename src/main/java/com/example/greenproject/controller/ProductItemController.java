package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateProductItemRequest;
import com.example.greenproject.dto.req.UpdateProductItemRequest;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.dto.res.ProductDtoView;
import com.example.greenproject.service.ProductItemService;
import com.example.greenproject.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productItems")
@RequiredArgsConstructor
public class ProductItemController {
    private final ProductItemService productItemService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getAllItemByProductId(@PathVariable Long productId){
        return ResponseEntity.ok().body(
                new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        productItemService.getAllProductItemsByProductId(productId)
                )
        );
    }



    @GetMapping
    public ResponseEntity<?> getAllCategories(@RequestParam(value = "pageNum",required = false) Integer pageNum,
                                              @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                              @RequestParam(value = "search",required = false) String search,
                                              @RequestParam(value = "productId",required = false) Long productId){

        Object products= productItemService.getAllProductItem(pageNum,pageSize,search,productId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        products));
    }




    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id")Long id,@RequestBody UpdateProductItemRequest updateProductItemRequest){

        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.CREATED.value(),
                Constants.SUCCESS_MESSAGE,
                productItemService.updateProductItem(id,updateProductItemRequest)
        ));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody CreateProductItemRequest createProductItemRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DataResponse(
                        HttpStatus.CREATED.value(),
                        Constants.SUCCESS_MESSAGE,
                        productItemService.createProductItem(createProductItemRequest)));
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id){
        productItemService.deleteProductItem(id);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                null));
    }
}
