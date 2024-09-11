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
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final static int PAGE_SIZE=5;

    public Object getAllCategories(Integer pageNum, Integer pageSize){
        if(pageNum==null || pageSize==null){
            return getAllCategoriesList();
        }

        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        Page<Category> categories = categoryRepository.findAll(pageable);
        List<CategoryDto> categoryDtos= categories.getContent().stream().map(Category::mapToCategoryDto).toList();
        return new PaginatedResponse<>(
                categoryDtos,
                categories.getTotalPages(),
                categories.getNumber()+1,
                categories.getTotalElements()
        );

    }

    private List<CategoryDto> getAllCategoriesList(){
        return categoryRepository.findAll().stream().map(Category::mapToCategoryDto).toList();
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

        Optional<Category> category = categoryRepository.findByNameIgnoreCase(createCategoryRequest.getName());
        if (category.isEmpty()){
            return categoryRepository.save(Category
                    .builder()
                    .name(createCategoryRequest.getName())
                    .parent(parentCategory )
                    .build());

        }else {
            throw new RuntimeException("Danh mục đã tồn tại!");
        }


    }

    public Category updateCategoryById(Long categoryId,UpdateCategoryRequest updateCategoryRequest){
        if(categoryId == null){
            throw new RuntimeException("Id danh mục rỗng!");
        }
        Category category = findCategoryById(categoryId);
        category.getChildren().forEach(c->{
            if(Objects.equals(c.getId(), updateCategoryRequest.getParentId())){
                throw new RuntimeException("Danh mục hiện tại là danh mục cha!");
            }
        });

        if(Objects.equals(category.getId(), updateCategoryRequest.getParentId())){
            throw new RuntimeException("Danh mục cha không được trùng với danh mục hiện tại!");
        }


        if(categoryRepository.countAllByNameIgnoreCaseAndIdNot(updateCategoryRequest.getName(),categoryId) == 1){
            throw new RuntimeException("Tên danh mục đã được sử dụng!");
        }

        if(updateCategoryRequest.getParentId() != null ){
            if((category.getParent()==null||!category.getParent().getId().equals(updateCategoryRequest.getParentId()))){
                Category parentCategory = categoryRepository.findById(updateCategoryRequest.getParentId())
                        .orElseThrow(()->new NotFoundException("Không tìm thấy danh mục cha với id : " + updateCategoryRequest.getParentId()));
                category.setParent(parentCategory);
            }

        }else {
            category.setParent(null);
        }

        category.setName(updateCategoryRequest.getName());
        return categoryRepository.save(category);






    }

    public void deleteCategoryById(Long id){
        categoryRepository.deleteById(id);
    }


    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new NotFoundException("Không tìm thấy danh mục với id : " + id));
    }

    public CategoryDto getCategoryById(Long id) {
        Category category= categoryRepository.findById(id).orElseThrow(()->new NotFoundException("Không tìm thấy danh mục với id : " + id));
        return category.mapToCategoryDto();
    }
}
