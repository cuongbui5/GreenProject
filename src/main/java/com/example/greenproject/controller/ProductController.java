package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateProductRequest;

import com.example.greenproject.dto.req.UpdateProductRequest;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.model.Product;
import com.example.greenproject.service.ProductService;
import com.example.greenproject.utils.Constants;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetails(@PathVariable("id") Long id){
        Product product = productService.findProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam(value = "pageNum",required = false) Integer pageNum,
                                              @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                              @RequestParam(value = "search",required = false) String search,
                                              @RequestParam(value = "categoryId",required = false) Long categoryId){



        return ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        "Successfully retrieved product list",
                        productService.getAllProduct(pageNum,pageSize,search,categoryId)));
    }

    @GetMapping("/view")
    public ResponseEntity<?> getAllProductViews(@RequestParam(value = "pageNum") Integer pageNum,
                                            @RequestParam(value = "pageSize") Integer pageSize){



        return ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        "Successfully retrieved product list",
                        productService.getAllProductViews(pageNum,pageSize)));
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id")Long id,@RequestBody UpdateProductRequest updateProductRequest){

        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.CREATED.value(),
                "Successfully created product",
                productService.updateProduct(id,updateProductRequest)
        ));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest createProductRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DataResponse(
                        HttpStatus.CREATED.value(),
                        "Successfully created product",
                        productService.createProduct(createProductRequest)));
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                null));
    }
}
