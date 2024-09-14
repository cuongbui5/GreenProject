package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateProductRequest;
import com.example.greenproject.dto.req.FilteringProductRequest;
import com.example.greenproject.dto.req.UpdateProductRequest;
import com.example.greenproject.dto.res.CategoryDtoWithParent;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.dto.res.ProductDto;
import com.example.greenproject.exception.NotFoundException;
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
    public Object getAllProduct(Integer pageNum, Integer pageSize, String search) {
        if(pageNum==null || pageSize==null){
            return getAllProduct();
        }
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        Page<Product> products;

        if(search!=null){
            products= searchProduct(search,pageable);
        }else {

            products = productRepository.findAll(pageable);
        }

        List<ProductDto> productDtos = products.getContent().stream().map(Product::mapToProductDto).toList();
        return new PaginatedResponse<>(
                productDtos,
                products.getTotalPages(),
                products.getNumber()+1,
                products.getTotalElements()
        );
    }

    private Page<Product> searchProduct(String search, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(search, pageable);
    }


    public List<ProductDto> getAllProduct(){
        return productRepository.findAll().stream().map(Product::mapToProductDto).toList();
    }



    public Product findProductById(Long id){
        return productRepository.findById(id).orElseThrow(()->new RuntimeException("Không tìm thấy sản phẩm với id: "+id));
    }



    public ProductDto createProduct(CreateProductRequest createProductRequest){
        Optional<Product> product = productRepository.findByName(createProductRequest.getName());
        if(product.isPresent()){
            throw new RuntimeException("Sản phẩm đã tồn tại!");
        }
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

    public Product updateProduct(Long productId,UpdateProductRequest updateProductRequest){
        Product product = productRepository.findById(productId).orElseThrow();
        if(updateProductRequest.getName() != null && !product.getName().equals(updateProductRequest.getName())){
            product.setName(updateProductRequest.getName());
        }
        if(updateProductRequest.getDescription() != null && !product.getDescription().equals(updateProductRequest.getDescription())){
            product.setDescription(updateProductRequest.getDescription());
        }
        if(updateProductRequest.getCategoryId() != null  && !product.getCategory().getId().equals(updateProductRequest.getCategoryId())){
            Category category = categoryRepository.findById(updateProductRequest.getCategoryId())
                    .orElseThrow(()->
                            new RuntimeException("Không tìm thấy danh mục với id:"+updateProductRequest.getCategoryId())
                    );
            product.setCategory(category);
        }
        return productRepository.save(product);
    }

    public void deleteProduct(Long id){
       productRepository.deleteById(id);
    }


}
