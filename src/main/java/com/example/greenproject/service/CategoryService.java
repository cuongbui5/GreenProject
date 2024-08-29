package com.example.greenproject.service;

import com.example.greenproject.dto.req.CategoryFilteringRequest;
import com.example.greenproject.dto.req.CreateCategoryRequest;
import com.example.greenproject.dto.req.UpdateCategoryRequest;
import com.example.greenproject.dto.req.UpdateVariationRequest;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.exception.category_exception.CategoryNotFoundException;
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
    private final VariationRepository variationRepository;

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

    private Page<Category> getAllCategoriesPagination(Pageable pageable){
        return categoryRepository.findAll(pageable);
    }

    public List<Category> getAllCategoriesParent(){
        return categoryRepository.getAllParentCategory();
    }

    public PaginatedResponse<Category> findByNameContaining(String name, Pageable pageable){
        Page<Category> categories = categoryRepository.findByNameContaining(name,pageable);

        return new PaginatedResponse<>(
                categories.getContent(),
                categories.getTotalPages(),
                categories.getNumber(),
                categories.getTotalElements()
        );
    }


    public PaginatedResponse<Category> filterCategory(CategoryFilteringRequest categoryFilteringRequest){
        String name = categoryFilteringRequest.getName();
        String sort = categoryFilteringRequest.getSortOption();
        int pageNum = categoryFilteringRequest.getPageNum();
        int pageSize = categoryFilteringRequest.getPageSize();

        var allCategories = categoryRepository.findAll();
        var filterCategories = allCategories.stream()
                .filter(category -> category.getName().contains(name))
                .sorted(sortOption(sort))
                .toList();
        Page<Category> categories = new PageImpl<>(filterCategories, PageRequest.of(pageNum,pageSize), filterCategories.size());

        return new PaginatedResponse<>(
                categories.getContent(),
                categories.getTotalPages(),
                categories.getNumber(),
                categories.getTotalElements()
        );
    }

    private Comparator<Category> sortOption(String sort){
        return switch (sort){
            case "create date" -> Comparator.comparing(Category::getCreatedAt);
            case "recently update" -> Comparator.comparing(Category::getUpdatedAt);
            default -> Comparator.comparing(Category::getName);
        };
    }

    public Category addCategory(CreateCategoryRequest createCategoryRequest){
        Category parentCategory  = null;
        Category category = null;
        if(createCategoryRequest.getParentId() != null){
            parentCategory  = categoryRepository.findById(createCategoryRequest.getParentId())
                    .orElseThrow(()->new CategoryNotFoundException("Not found parent category id " + createCategoryRequest.getParentId()));

            parentCategory.getChildren().add(category);
        }

         category = categoryRepository.findByName(createCategoryRequest.getName())
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

    public Category updateCategory(Long categoryId,UpdateCategoryRequest updateCategoryRequest){
        if(categoryId == null){
            throw new RuntimeException();
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new CategoryNotFoundException("Not found category id " + categoryId));

        if(updateCategoryRequest.getParentId() != null){
            Category parentCategory = categoryRepository.findById(updateCategoryRequest.getParentId())
                    .orElseThrow(()->new CategoryNotFoundException("Not found parent category id " + updateCategoryRequest.getParentId()));
            category.setParent(parentCategory);
        }

        category.setName(updateCategoryRequest.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new CategoryNotFoundException("Not found category id " + id));

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


    // ---------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------//
    public Variation addVariationInCategory(Long categoryId, Long variationId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new CategoryNotFoundException("Not found category id " + categoryId));

        Variation variation = variationRepository.findById(variationId)
                .orElseThrow();

        Optional<Variation> alreadyExistVariation = category.getVariations()
                .stream()
                .filter(v -> v.getId().equals(variation.getId()))
                .findFirst();
        if(alreadyExistVariation.isEmpty()){
            category.addVariation(variation);
            Category saveCategory = categoryRepository.save(category);
            return saveCategory.getVariations()
                    .stream()
                    .filter(v -> v.getId().equals(variation.getId()))
                    .findFirst()
                    .orElseThrow();
        }
        throw new RuntimeException("Already exist variation " + variation.getName() + "in category " + alreadyExistVariation.get().getName());
    }

    public void deleteVariationFromCategory(Long variationId,Long categoryId){
        Variation variation = variationRepository.findById(variationId).orElseThrow();
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new CategoryNotFoundException("Not found category id " + categoryId));
        category.removeVariation(variation);
        categoryRepository.save(category);
    }
    // ---------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------//
}
