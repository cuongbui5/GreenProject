package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateProductRequest;
import com.example.greenproject.dto.req.FilteringProductRequest;
import com.example.greenproject.dto.req.UpdateProductRequest;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.model.Product;
import com.example.greenproject.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Successfully retriever product details id " + id,
                product));
    }

    @GetMapping
    public ResponseEntity<?> getAllProduct(@RequestParam(value = "pageNum",required = false) Integer pageNum,
                                           @RequestParam(value = "pageSize",required = false) Integer pageSize){
        var products = productService.getAllProduct(pageNum,pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(
                new DataResponse(
                HttpStatus.OK.value(),
                "Successfully retriever product list",
                products));
    }

    @GetMapping("/search/name={keyword}/page={pageNum}")
    public ResponseEntity<?> findProductByNameContaining(@PathVariable("keyword") String keyword, @PathVariable("pageNum") int pageNum){
        PaginatedResponse<Product> products = productService.findByNameContaining(keyword,PageRequest.of(pageNum,10));
        return ResponseEntity.status(HttpStatus.OK).body(
                new DataResponse(
                        HttpStatus.OK.value(),
                        "Successfully retriever product list by searching",
                        products));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id")Long id,@RequestBody UpdateProductRequest updateProductRequest){
        Product updateProduct = productService.updateProduct(id,updateProductRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new DataResponse(
                        HttpStatus.OK.value(),
                        "Successfully update product list",
                        updateProduct));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest createProductRequest){
        Product createProduct = productService.createProduct(createProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new DataResponse(
                        HttpStatus.CREATED.value(),
                        "Successfully create new product",
                        createProduct));
    }

    @GetMapping("/filtering")
    public ResponseEntity<?> filteringProduct(@RequestBody FilteringProductRequest productFilteringRequest){
        PaginatedResponse<Product> products = productService.filteringProduct(productFilteringRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new DataResponse(
                        HttpStatus.CREATED.value(),
                        "Successfully retriever product list by filter",
                        products));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new DataResponse(
                        HttpStatus.CREATED.value(),
                        "Successfully delete product id " + id,
                        null));
    }
}
