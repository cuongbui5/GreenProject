package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CategoryFilteringRequest;
import com.example.greenproject.dto.req.CreateCategoryRequest;
import com.example.greenproject.dto.req.UpdateCategoryRequest;
import com.example.greenproject.model.Category;
import com.example.greenproject.model.Variation;
import com.example.greenproject.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        var categories = categoryService.getAllCategories(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @PostMapping("/create")
    public ResponseEntity<?> addCategory(@RequestBody CreateCategoryRequest createCategoryRequest){
        Category saveCategory = categoryService.addCategory(createCategoryRequest);
        return ResponseEntity.status(HttpStatus.OK).body(saveCategory);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long categoryId,@RequestBody UpdateCategoryRequest updateCategoryRequest){
        Category updateCategory = categoryService.updateCategory(categoryId,updateCategoryRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updateCategory);
    }

    @GetMapping("/search/name={keyword}/page={pageNum}")
    public ResponseEntity<?> findCategoryByNameContaining(@PathVariable("keyword") String keyword, @PathVariable("pageNum") int pageNum){
        Page<Category> categories = categoryService.findByNameContaining(keyword,PageRequest.of(pageNum,10));
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @GetMapping("/filtering")
    public ResponseEntity<?> filteringCategory(@RequestBody CategoryFilteringRequest categoryFilteringRequest){
        Page<Category> categories = categoryService.filterCategory(categoryFilteringRequest);
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete success category id " + id);
    }

    @PostMapping("/{categoryId}/variations/add/{variationId}")
    public ResponseEntity<?> addVariationToCategory(@PathVariable("categoryId") Long categoryId,@PathVariable("variationId") Long variationId){
        Variation saveVariation = categoryService.addVariationInCategory(categoryId,variationId);
        return ResponseEntity.status(HttpStatus.OK).body(saveVariation);
    }

    @PostMapping("/{categoryId}/delete/{variationId}")
    public ResponseEntity<?> deleteVariationFromCategory(@PathVariable("categoryId") Long categoryId,@PathVariable("variationId") Long variationId){
        categoryService.deleteVariationFromCategory(variationId,categoryId);
        return ResponseEntity.status(HttpStatus.OK).body("Delete success variation from category " + categoryId);
    }
}
