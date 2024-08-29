package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CategoryFilteringRequest;
import com.example.greenproject.dto.req.CreateCategoryRequest;
import com.example.greenproject.dto.req.UpdateCategoryRequest;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.model.Category;
import com.example.greenproject.model.Variation;
import com.example.greenproject.service.CategoryService;
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
                                              @RequestParam(value = "pageSize",required = false) Integer pageSize){

        var categories= (pageNum == null || pageSize == null) ? categoryService.getAllCategories() :
            categoryService.getAllCategories(PageRequest.of(pageNum,pageSize));

        if(categories instanceof Page<Category> temp){
            PaginatedResponse<Category> response = new PaginatedResponse<>(
                    temp.getContent(),
                    temp.getTotalPages(),
                    temp.getNumber(),
                    temp.getTotalElements()
            );
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new DataResponse(
                            HttpStatus.OK.value(),
                            "Successfully retrieved category list",
                            response));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DataResponse(
                        HttpStatus.OK.value(),
                        "Successfully retrieved category list",
                        categories));
    }

    @PostMapping("/create")
    public ResponseEntity<?> addCategory(@RequestBody CreateCategoryRequest createCategoryRequest){
        Category saveCategory = categoryService.addCategory(createCategoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse(
                HttpStatus.CREATED.value(),
                "Successfully create new category " + saveCategory.getName(),
                saveCategory));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long categoryId,@RequestBody UpdateCategoryRequest updateCategoryRequest){
        Category updateCategory = categoryService.updateCategory(categoryId,updateCategoryRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Successfully update category " + updateCategory.getId(),
                updateCategory));
    }

    @GetMapping("/search/name={keyword}/page={pageNum}")
    public ResponseEntity<?> findCategoryByNameContaining(@PathVariable("keyword") String keyword, @PathVariable("pageNum") int pageNum){
        Page<Category> categories = categoryService.findByNameContaining(keyword,PageRequest.of(pageNum,10));
        PaginatedResponse<Category> response = new PaginatedResponse<>(
                categories.getContent(),
                categories.getTotalPages(),
                categories.getNumber(),
                categories.getTotalElements()
        );
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Successfully retrieved category list",
                response));
    }

    @GetMapping("/filtering")
    public ResponseEntity<?> filteringCategory(@RequestBody CategoryFilteringRequest categoryFilteringRequest){
        Page<Category> categories = categoryService.filterCategory(categoryFilteringRequest);

        PaginatedResponse<Category> response = new PaginatedResponse<>(
                categories.getContent(),
                categories.getTotalPages(),
                categories.getNumber(),
                categories.getTotalElements()
        );
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Successfully filtering category ",
                response));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Delete success category id " + id,
                null));
    }



    // ---------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------//
    @PostMapping("/{categoryId}/variations/add/{variationId}")
    public ResponseEntity<?> addVariationToCategory(@PathVariable("categoryId") Long categoryId,@PathVariable("variationId") Long variationId){
        Variation saveVariation = categoryService.addVariationInCategory(categoryId,variationId);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Successful add variation " +saveVariation.getName() + " to category id " + saveVariation.getName(),
                saveVariation));
    }

    @PostMapping("/{categoryId}/delete/{variationId}")
    public ResponseEntity<?> deleteVariationFromCategory(@PathVariable("categoryId") Long categoryId,@PathVariable("variationId") Long variationId){
        categoryService.deleteVariationFromCategory(variationId,categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Delete success variation " +variationId + " from category " + categoryId,
                null
        ));
    }
    // ---------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------//
}
