package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateProductRequest;

import com.example.greenproject.dto.req.UpdateProductRequest;

import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.dto.res.ProductDto;
import com.example.greenproject.dto.res.ProductDtoView;
import com.example.greenproject.exception.NotFoundException;
import com.example.greenproject.model.Category;
import com.example.greenproject.model.Product;
import com.example.greenproject.repository.CategoryRepository;
import com.example.greenproject.repository.ProductDtoViewRepository;
import com.example.greenproject.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductDtoViewRepository productDtoViewRepository;

    public Object getAllProductsView(Integer pageNum, Integer pageSize,Long categoryId,String search){
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        Page<ProductDtoView> products = null;
        if(search==null&&categoryId==null){
           products = productDtoViewRepository.findAll(pageable);
        }

        if(search==null&&categoryId!=null){
            List<Long> categoryIds = new ArrayList<>();
            collectChildCategoryIds(categoryId, categoryIds);
            products=productDtoViewRepository.findByCategoryId(categoryIds,pageable);
        }

        if (search != null && categoryId == null) {
            products = productDtoViewRepository.findByNameContainingIgnoreCase(search,pageable);
        }



        return new PaginatedResponse<>(
                products.getContent(),
                products.getTotalPages(),
                products.getNumber()+1,
                products.getTotalElements()
        );
    }



    public Object getAllSortedProductItems(int pageNumber, int pageSize, String option) {

        Pageable pageable = switch (option) {
            case "ascMinPrice" -> PageRequest.of(pageNumber - 1, pageSize, Sort.by("minPrice").ascending());
            case "descMinPrice" -> PageRequest.of(pageNumber - 1, pageSize, Sort.by("minPrice").descending());
            case "ascMaxPrice" -> PageRequest.of(pageNumber - 1, pageSize, Sort.by("maxPrice").ascending());
            case "descMaxPrice" -> PageRequest.of(pageNumber - 1, pageSize, Sort.by("maxPrice").descending());
            case "reviewCount" -> PageRequest.of(pageNumber - 1, pageSize, Sort.by("totalReviews").descending());
            case "totalRating" -> PageRequest.of(pageNumber - 1, pageSize, Sort.by("totalRating").descending());
            default -> PageRequest.of(pageNumber - 1, pageSize);
        };

        Page<ProductDtoView> productDtoViews = productDtoViewRepository.findAll(pageable);

        return new PaginatedResponse<>(
                productDtoViews.getContent(),
                productDtoViews.getTotalPages(),
                productDtoViews.getNumber()+1,
                productDtoViews.getTotalElements()
        );
    }






    public PaginatedResponse<ProductDtoView> getProductsByTopSold(Integer pageNum,Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        Page<ProductDtoView> productDtoViews = productDtoViewRepository.findByTopSold(pageable);

        return new PaginatedResponse<>(
                productDtoViews.getContent(),
                productDtoViews.getTotalPages(),
                productDtoViews.getNumber()+1,
                productDtoViews.getTotalElements()
        );
    }





    private void collectChildCategoryIds(Long categoryId, List<Long> categoryIds) {
        categoryIds.add(categoryId);
        List<Category> childCategories = categoryRepository.findByParentId(categoryId);

        for (Category child : childCategories) {
            collectChildCategoryIds(child.getId(), categoryIds);
        }
    }




    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }



    public Product findProductById(Long id){
        return productRepository.findById(id).orElseThrow(()->new RuntimeException("Không tìm thấy sản phẩm với id: "+id));
    }



    public ProductDto createProduct(CreateProductRequest createProductRequest){
        Category category=null;
        if(createProductRequest.getCategoryId()!=null){
            category = categoryRepository
                    .findById(createProductRequest.getCategoryId())
                    .orElseThrow(()-> new NotFoundException("Not found category"));
        }

        Product newProduct = Product
                .builder()
                .name(createProductRequest.getName())
                .description(createProductRequest.getDescription())
                .category(category)
                .build();

        return productRepository.save(newProduct).mapToProductDto();
    }
    @Transactional
    public ProductDto updateProduct(Long productId,UpdateProductRequest updateProductRequest){

        Product product = productRepository.findById(productId).orElseThrow(()->new NotFoundException("Khong tim thay san pham"));
        if(updateProductRequest.getCategoryId() != null && !product.getCategory().getId().equals(updateProductRequest.getCategoryId())){
            if(!product.getProductItems().isEmpty()){
                throw new RuntimeException("Sản phẩm này đã có chi tiết sản phẩm việc cập nhật danh mục là sai logic hệ thống!");
            }


            Category category = categoryRepository.findById(updateProductRequest.getCategoryId())
                    .orElseThrow(()->
                            new RuntimeException("Không tìm thấy danh mục với id:"+updateProductRequest.getCategoryId())
                    );
            product.setCategory(category);
        }

        product.setName(updateProductRequest.getName());
        product.setDescription(updateProductRequest.getDescription());

        return productRepository.save(product).mapToProductDto();
    }

    public void deleteProduct(Long id){

       productRepository.deleteById(id);
    }





    public Object getProductById(Long productId) {
        Product product=productRepository.findByProductId(productId).orElseThrow(()->new RuntimeException("Khong tim thay san pham"));
        return product.mapToProductDto();
    }
}
