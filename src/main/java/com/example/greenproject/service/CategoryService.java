package com.example.greenproject.service;

import com.example.greenproject.dto.req.CategoryFilteringRequest;
import com.example.greenproject.dto.req.CreateCategoryRequest;
import com.example.greenproject.dto.req.UpdateCategoryRequest;
import com.example.greenproject.dto.res.CategoryDto;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.exception.NotFoundException;
import com.example.greenproject.model.Category;
import com.example.greenproject.model.Variation;
import com.example.greenproject.repository.CategoryRepository;
import com.example.greenproject.repository.VariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;


    public Object getAllCategories(Integer pageNum, Integer pageSize){
        var categories= (pageNum == null || pageSize == null) ? getAllCategoriesList() :
                getAllCategoriesPagination(PageRequest.of(pageNum,pageSize));

        if(categories instanceof Page<Category> temp) {
            return new PaginatedResponse<>(
                    temp.getContent(),
                    temp.getTotalPages(),
                    temp.getNumber(),
                    temp.getTotalElements()
            );
        }
        return categories;
    }

    private List<Category> getAllCategoriesList(){
        return categoryRepository.findAll();
    }

    public List<Category> getAllParents(){
        return categoryRepository.getCategoriesByParentIsNull();
    }

    private Page<Category> getAllCategoriesPagination(Pageable pageable){
        return categoryRepository.findAll(pageable);
    }



    public Category createCategory(CreateCategoryRequest createCategoryRequest){
        Category parentCategory  = null;
        if(createCategoryRequest.getParentId() != null){
            parentCategory  = findCategoryById(createCategoryRequest.getParentId());
        }

        Category category = categoryRepository.findByName(createCategoryRequest.getName())
                .orElse(Category
                        .builder()
                        .name(createCategoryRequest.getName())
                        .parent(parentCategory )
                        .build());
        if(category.getId() != null){
            throw new RuntimeException("Already exist category");
        }
        return categoryRepository.save(category);
    }

    public Category updateCategoryById(Long categoryId,UpdateCategoryRequest updateCategoryRequest){
        if(categoryId == null){
            throw new RuntimeException();
        }
        Category category = findCategoryById(categoryId);

        if(updateCategoryRequest.getParentId() != null){
            Category parentCategory = categoryRepository.findById(updateCategoryRequest.getParentId())
                    .orElseThrow(()->new NotFoundException("Not found parent category id " + updateCategoryRequest.getParentId()));
            category.setParent(parentCategory);
        }

        category.setName(updateCategoryRequest.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategoryById(Long id){
        Category category =findCategoryById(id);

        if(category.getParent() != null){
            category.setParent(null);
        }else{
            var categories = categoryRepository.findByParentId(id);
            categories.forEach(c -> {
                c.setParent(null);
                categoryRepository.save(c);
            });
        }
        categoryRepository.deleteById(id);
    }


    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new NotFoundException("Not found category id " + id));
    }

    public CategoryDto getCategoryById(Long id) {
        Category category= categoryRepository.findById(id).orElseThrow(()->new NotFoundException("Not found category id " + id));
        return category.mapToCategoryDto();
    }
}
