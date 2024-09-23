package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateCategoryRequest;
import com.example.greenproject.dto.req.UpdateCategoryRequest;
import com.example.greenproject.dto.res.CategoryDto;
import com.example.greenproject.dto.res.CategoryDtoWithChild;
import com.example.greenproject.dto.res.CategoryDtoWithParent;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.exception.NotFoundException;
import com.example.greenproject.model.Category;
import com.example.greenproject.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final RedisService redisService;
    private static final String CATEGORY_KEY = "categories";
    private static final long CACHE_TTL = 1;


    public Object getAllCategories(Integer pageNum, Integer pageSize, String search) {
        if (pageNum == null || pageSize == null) {
            return getAllCategoriesList();
        }

        /*String redisKey = CATEGORY_KEY + ":" + pageNum + ":" + pageSize + (search != null ? ":search:" + search : "");
        Object cachedObject = redisService.get(redisKey);

        if (cachedObject != null) {

            if (cachedObject instanceof PaginatedResponse) {
                System.out.println("Get data cache");
                return cachedObject;
            } else {
                throw new IllegalStateException("Data type mismatch in cache");
            }
        }*/

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Category> categories = search != null ? searchCategory(search, pageable) : categoryRepository.findAll(pageable);

        List<CategoryDtoWithParent> categoryDtoWithParents = categories.getContent().stream()
                .map(Category::mapToCategoryDtoWithParent)
                .toList();

        // Cache the result
        //redisService.set(redisKey, paginatedResponse);
        //redisService.setTimeToLive(redisKey, CACHE_TTL);

        return new PaginatedResponse<>(
                categoryDtoWithParents,
                categories.getTotalPages(),
                categories.getNumber() + 1,
                categories.getTotalElements()
        );
    }


    private Page<Category> searchCategory(String search,Pageable pageable){
        return categoryRepository.findByNameContainingIgnoreCase(search,pageable);
    }

    private List<CategoryDtoWithParent> getAllCategoriesList(){
        return categoryRepository.findAll().stream().map(Category::mapToCategoryDtoWithParent).toList();
    }

    public List<CategoryDtoWithChild> getAllParents(){
        return categoryRepository.getCategoriesByParentIsNull().stream().map(Category::mapToCategoryDtoWithChild).toList();
    }

    private Page<Category> getAllCategoriesPagination(Pageable pageable){
        return categoryRepository.findAll(pageable);
    }



    public CategoryDtoWithParent createCategory(CreateCategoryRequest createCategoryRequest){
        Category parentCategory  = null;
        if(createCategoryRequest.getParentId() != null){
            parentCategory  = findCategoryById(createCategoryRequest.getParentId());
        }

        return categoryRepository.save(Category
                .builder()
                .name(createCategoryRequest.getName())
                .parent(parentCategory )
                .build()).mapToCategoryDtoWithParent();


    }

    public CategoryDtoWithParent updateCategoryById(Long categoryId, UpdateCategoryRequest updateCategoryRequest){
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
        return categoryRepository.save(category).mapToCategoryDtoWithParent();

    }

    public void deleteCategoryById(Long id){
        Optional<Category> categoryOptional=categoryRepository.findById(id);
        if(categoryOptional.isPresent()){
            Category category=categoryOptional.get();
            if(!category.getChildren().isEmpty()){
                throw new RuntimeException("Danh mục này đang có nhiều danh mục con! Thao tác thất bại!");
            }

            if(!category.getProducts().isEmpty()){
                throw new RuntimeException("Danh mục này đang có nhiều san pham! Thao tác thất bại!");
            }

            if(!category.getVariations().isEmpty()){
                throw new RuntimeException("Danh mục này đang có nhiều bien the! Thao tác thất bại!");
            }
            categoryRepository.delete(category);
        }

    }


    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new NotFoundException("Không tìm thấy danh mục với id : " + id));
    }

    public CategoryDtoWithParent getCategoryById(Long id) {
        Category category= categoryRepository.findById(id).orElseThrow(()->new NotFoundException("Không tìm thấy danh mục với id : " + id));
        return category.mapToCategoryDtoWithParent();
    }
}
