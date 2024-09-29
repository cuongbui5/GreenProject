package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateProductRequest;

import com.example.greenproject.dto.req.UpdateProductRequest;
import com.example.greenproject.dto.res.DataResponse;
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


    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam(value = "pageNum",required = false) Integer pageNum,
                                            @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                            @RequestParam(value = "search",required = false) String search,
                                            @RequestParam(value = "categoryId",required = false) Long categoryId){



        return ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        productService.getAllProduct(pageNum,pageSize,search,categoryId)));
    }


    @GetMapping("/view")
    public ResponseEntity<?> getAllProductViews(@RequestParam(value = "pageNum") Integer pageNum,
                                            @RequestParam(value = "pageSize") Integer pageSize){



        return ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        productService.getAllProductsView(pageNum,pageSize)));
    }

    @GetMapping("/related_product")
    public ResponseEntity<?> getProductByCategoryId(@RequestParam(value = "pageNum",required = false) Integer pageNum,
                                                  @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                                  @RequestParam(value = "categoryId",required = false) Long categoryId){
        return  ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        productService.getProductByCategoryId(pageNum,pageSize,categoryId)));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductViews(@PathVariable Long productId){


        return ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        productService.getProductById(productId)));
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id")Long id,@RequestBody UpdateProductRequest updateProductRequest){

        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.CREATED.value(),
                Constants.SUCCESS_MESSAGE,
                productService.updateProduct(id,updateProductRequest)
        ));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest createProductRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DataResponse(
                        HttpStatus.CREATED.value(),
                        Constants.SUCCESS_MESSAGE,
                        productService.createProduct(createProductRequest)));
    }

    @GetMapping("/top_sold")
    public ResponseEntity<?> getProductItemByTopSold(@RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                                     @RequestParam(value = "pageSize",required = false,defaultValue = "8") Integer pageSize){

        return ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        productService.getProductsByTopSold(pageNum,pageSize)));
    }


    @GetMapping("/sort")
    public ResponseEntity<?> getAllSortedProduct(@RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                                 @RequestParam(value = "pageSize",required = false,defaultValue = "50") Integer pageSize){

        return ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        productService.getAllSortedProductItems(pageNum,pageSize)));
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
