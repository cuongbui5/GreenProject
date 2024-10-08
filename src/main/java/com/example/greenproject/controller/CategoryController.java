package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CategoryFilteringRequest;
import com.example.greenproject.dto.req.CreateCategoryRequest;
import com.example.greenproject.dto.req.UpdateCategoryRequest;
import com.example.greenproject.dto.res.BaseResponse;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.model.Category;
import com.example.greenproject.model.Variation;
import com.example.greenproject.service.CategoryService;
import com.example.greenproject.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping
    public ResponseEntity<?> getAllCategories(@RequestParam(value = "pageNum",required = false) Integer pageNum,
                                              @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                              @RequestParam(value = "search",required = false) String search){

        Object categories= categoryService.getAllCategories(pageNum,pageSize,search);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        categories));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id){

        return ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        categoryService.getCategoryById(id)));
    }

    @PostMapping("/create")
    public ResponseEntity<?> addCategory(@RequestBody CreateCategoryRequest createCategoryRequest){

        return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse(
                HttpStatus.CREATED.value(),
                Constants.SUCCESS_MESSAGE,
                categoryService.createCategory(createCategoryRequest)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long categoryId,@RequestBody UpdateCategoryRequest updateCategoryRequest){


        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                categoryService.updateCategoryById(categoryId,updateCategoryRequest)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE
        ));
    }



}
