package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateCategoryRequest;
import com.example.greenproject.dto.req.UpdateCategoryRequest;
import com.example.greenproject.dto.res.CategoryDto;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.exception.NotFoundException;
import com.example.greenproject.model.Category;
import com.example.greenproject.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    //private final RedisService;
    //private static final String CATEGORY_KEY = "categories";
    //private static final long CACHE_TTL = 1;



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

        List<CategoryDto> categoryDtos = categories.getContent().stream()
                .map(Category::mapToCategoryDto)
                .toList();

        // Cache the result
        //redisService.set(redisKey, paginatedResponse);
        //redisService.setTimeToLive(redisKey, CACHE_TTL);

        return new PaginatedResponse<>(
                categoryDtos,
                categories.getTotalPages(),
                categories.getNumber() + 1,
                categories.getTotalElements()
        );
    }


    private Page<Category> searchCategory(String search,Pageable pageable){
        return categoryRepository.findByNameContainingIgnoreCase(search,pageable);
    }

    private List<CategoryDto> getAllCategoriesList(){
        return categoryRepository.findAll().stream().map(Category::mapToCategoryDto).toList();
    }







    public CategoryDto createCategory(CreateCategoryRequest createCategoryRequest){
        Category parentCategory  = null;
        if(createCategoryRequest.getParentId() != null){
            parentCategory  = findCategoryById(createCategoryRequest.getParentId());
        }

        return categoryRepository.save(Category
                .builder()
                .name(createCategoryRequest.getName())
                .parent(parentCategory )
                .build()).mapToCategoryDto();


    }
    /*if(categoryId == null){
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
       }*/
    @Transactional
    public CategoryDto updateCategoryById(Long categoryId, UpdateCategoryRequest updateCategoryRequest){
        Category category = findCategoryById(categoryId);
        Category parentCategory = null;
        if(Objects.equals(updateCategoryRequest.getParentId(), categoryId)){
            throw new RuntimeException("Data không hợp lệ!");
        }
        if(updateCategoryRequest.getParentId() != null){
            parentCategory = findCategoryById(updateCategoryRequest.getParentId());
        }

        category.setName(updateCategoryRequest.getName());
        category.setParent(parentCategory);
        return categoryRepository.save(category).mapToCategoryDto();

    }
    @Transactional
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
