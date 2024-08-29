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
        Category existOrNotCategoryParent = null;
        if(createCategoryRequest.getParentId() != null){
            existOrNotCategoryParent = categoryRepository.findById(createCategoryRequest.getParentId()).orElseThrow();
        }

        Category existOrNotCategory = categoryRepository.findByName(createCategoryRequest.getName())
                .orElse(Category
                        .builder()
                        .name(createCategoryRequest.getName())
                        .parent(existOrNotCategoryParent)
                        .build());
        if(existOrNotCategory.getId() != null){
            throw new RuntimeException("Already exist category");
        }
        return categoryRepository.save(existOrNotCategory);
    }

    public Category updateCategory(Long categoryId,UpdateCategoryRequest updateCategoryRequest){
        if(categoryId == null){
            throw new RuntimeException();
        }
        Category existOrNotCategory = categoryRepository.findById(categoryId)
                .orElseThrow(()->new CategoryNotFoundException("Not found category id " + categoryId));

        if(updateCategoryRequest.getParentId() != null){
            Category existOrNotCategoryParent = categoryRepository.findById(updateCategoryRequest.getParentId())
                    .orElseThrow(()->new CategoryNotFoundException("Not found parent category id " + updateCategoryRequest.getParentId()));
            existOrNotCategory.setParent(existOrNotCategoryParent);
        }

        existOrNotCategory.setName(updateCategoryRequest.getName());
        return categoryRepository.save(existOrNotCategory);
    }

    public void deleteCategory(Long id){
        Category existOrNotCategory = categoryRepository.findById(id)
                .orElseThrow(()->new CategoryNotFoundException("Not found category id " + id));

        if(existOrNotCategory.getParent() != null){
            existOrNotCategory.setParent(null);
        }else{
            var categories = categoryRepository.findByParentId(id);
            categories.forEach(category -> categoryRepository.deleteById(category.getId()));
        }
        categoryRepository.deleteById(id);
    }


    public Variation addVariationInCategory(Long categoryId, Long variationId){
        Category existOrNotCategory = categoryRepository.findById(categoryId).orElseThrow(()->new CategoryNotFoundException("Not found category id " + categoryId));

        Variation existOrNotVariation = variationRepository.findById(variationId)
                .orElseThrow();

        Optional<Variation> alreadyExistVariation = existOrNotCategory.getVariations()
                .stream()
                .filter(variation -> variation.getId().equals(existOrNotVariation.getId()))
                .findFirst();
        if(alreadyExistVariation.isEmpty()){
            existOrNotCategory.addVariation(existOrNotVariation);
            Category saveCategory = categoryRepository.save(existOrNotCategory);
            return saveCategory.getVariations()
                    .stream()
                    .filter(variation -> variation.getId().equals(existOrNotVariation.getId()))
                    .findFirst()
                    .orElseThrow();
        }
        throw new RuntimeException("Already exist variation " + existOrNotVariation.getName() + "in category " + alreadyExistVariation.get().getName());
    }

    public void deleteVariationFromCategory(Long variationId,Long categoryId){
        Variation existOrNotVariation = variationRepository.findById(variationId).orElseThrow();
        Category existOrNotCategory = categoryRepository.findById(categoryId).orElseThrow(()->new CategoryNotFoundException("Not found category id " + categoryId));
        existOrNotCategory.removeVariation(existOrNotVariation);
        categoryRepository.save(existOrNotCategory);
    }
}
