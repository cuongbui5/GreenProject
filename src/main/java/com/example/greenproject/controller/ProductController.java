package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateProductRequest;
import com.example.greenproject.dto.req.FilteringProductRequest;
import com.example.greenproject.dto.req.UpdateProductRequest;
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
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping
    public ResponseEntity<?> getAllProduct(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize){
        Page<Product> products = productService.getAllProduct(PageRequest.of(pageNum,pageSize));
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/search/name={keyword}/page={pageNum}")
    public ResponseEntity<?> findProductByNameContaining(@PathVariable("keyword") String keyword, @PathVariable("pageNum") int pageNum){
        Page<Product> products = productService.findByNameContaining(keyword,PageRequest.of(pageNum,10));
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id")Long id,@RequestBody UpdateProductRequest updateProductRequest){
        Product updateProduct = productService.updateProduct(id,updateProductRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest createProductRequest){
        Product createProduct = productService.createProduct(createProductRequest);
        return ResponseEntity.status(HttpStatus.OK).body(createProduct);
    }

    @GetMapping("/filtering")
    public ResponseEntity<?> filteringProduct(@RequestBody FilteringProductRequest productFilteringRequest){
        Page<Product> products = productService.filteringProduct(productFilteringRequest);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete success product id " + id);
    }
}
