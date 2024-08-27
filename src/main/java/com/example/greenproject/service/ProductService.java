package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateProductRequest;
import com.example.greenproject.dto.req.FilteringProductRequest;
import com.example.greenproject.dto.req.UpdateProductRequest;
import com.example.greenproject.model.Category;
import com.example.greenproject.model.Image;
import com.example.greenproject.model.Product;
import com.example.greenproject.repository.CategoryRepository;
import com.example.greenproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Page<Product> getAllProduct(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    public Page<Product> findByNameContaining(String keyword, Pageable pageable){
        return productRepository.findByNameContaining(keyword,pageable);
    }

    public Product findProductById(Long id){
        return productRepository.findById(id).orElseThrow();
    }

    public Page<Product> filteringProduct(FilteringProductRequest filteringProductRequest){
        int pageNum = filteringProductRequest.getPageNum();
        int pageSize = filteringProductRequest.getPageSize();
        String name = filteringProductRequest.getName();
        Long categoryId = filteringProductRequest.getCategoryId();

        List<Product> productList = productRepository
                .findAll()
                .stream()
                .filter(product -> product.getName().contains(name))
                .filter(product ->product.getCategory().getId().equals(categoryId) ||
                        (product.getCategory().getParent() != null && product.getCategory().getParent().getId().equals(categoryId)))
                .toList();

        return new PageImpl<>(productList, PageRequest.of(pageNum,pageSize),productList.size());
    }

    public Product createProduct(CreateProductRequest createProductRequest){
        Optional<Product> existOrNotProduct = productRepository.findByName(createProductRequest.getName());
        if(existOrNotProduct.isPresent()){
            return null;
        }

        Long categoryId = createProductRequest.getCategoryId();
        Category existOrNotCategory = categoryRepository.findById(categoryId).orElseThrow(()->new RuntimeException("Not found category"));
        List<Image> convertImageList = createProductRequest.getImagesUrl()
                .stream()
                .map((image)-> Image.
                        builder()
                        .url(image)
                        .build())
                .toList();


        Product tempProduct = Product
                .builder()
                .name(createProductRequest.getName())
                .description(createProductRequest.getDescription())
                .category(existOrNotCategory)
                .build();
        tempProduct.addImage(convertImageList);
        return productRepository.save(tempProduct);
    }

    public Product updateProduct(Long productId,UpdateProductRequest updateProductRequest){
        Product existOrNotProduct = productRepository.findById(productId).orElseThrow();
        if(updateProductRequest.getName() != null && !existOrNotProduct.getName().equals(updateProductRequest.getName())){
            existOrNotProduct.setName(updateProductRequest.getName());
        }
        if(updateProductRequest.getDescription() != null && !existOrNotProduct.getDescription().equals(updateProductRequest.getDescription())){
            existOrNotProduct.setDescription(updateProductRequest.getDescription());
        }
        if(updateProductRequest.getCategoryId() != null  && !existOrNotProduct.getCategory().getId().equals(updateProductRequest.getCategoryId())){
            Category existOrNotCategory = categoryRepository.findById(updateProductRequest.getCategoryId()).orElseThrow();
            existOrNotProduct.setCategory(existOrNotCategory);
        }
        return productRepository.save(existOrNotProduct);
    }

    public void deleteProduct(Long id){
        Product existOrNotProduct = productRepository.findById(id).orElseThrow();
        existOrNotProduct.setCategory(null);
        productRepository.delete(existOrNotProduct);
    }
}
