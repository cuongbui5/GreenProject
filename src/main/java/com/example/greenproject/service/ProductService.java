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

    public Object getAllProductsView(Integer pageNum, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        Page<ProductDtoView> products = productDtoViewRepository.findAll(pageable);

        return new PaginatedResponse<>(
                products.getContent(),
                products.getTotalPages(),
                products.getNumber()+1,
                products.getTotalElements()
        );
    }

    public Object getAllProduct(Integer pageNum, Integer pageSize, String search,Long categoryId) {
        System.out.println(pageSize);
        if(pageNum==null || pageSize==null){
            return getAllProduct();
        }

        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        Page<Product> products = null;

        
        if(search==null&&categoryId==null){
            products = productRepository.findAll(pageable);
        }

        // Nếu chỉ có search, tìm sản phẩm theo từ khóa
        if (search != null && categoryId == null) {
            products = searchProduct(search, pageable);
        }

        // Nếu chỉ có categoryId, tìm sản phẩm theo category (bao gồm các category con)
        if (categoryId != null && search == null) {
            products = getProductsByCategory(categoryId, pageable);
        }

        // Nếu cả search và categoryId đều khác null, kết hợp tìm kiếm và lọc theo category
        if (categoryId != null && search != null) {
            products = searchProductByCategory(search, categoryId, pageable);
        }


        return new PaginatedResponse<>(
                products.getContent().stream().map(Product::mapToProductDto).toList(),
                products.getTotalPages(),
                products.getNumber()+1,
                products.getTotalElements()
        );
    }

    public Object getAllSortedProductItems(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        Page<ProductDtoView> productDtoViews = productDtoViewRepository.findAllProductsOrderByLowestPrice(pageable);

        return new PaginatedResponse<>(
                productDtoViews.getContent(),
                productDtoViews.getTotalPages(),
                productDtoViews.getNumber()+1,
                productDtoViews.getTotalElements()
        );
    }


    public Object getProductByCategoryId (Integer pageNum, Integer pageSize,Long categoryId){
        List<Long> categoryIds = new ArrayList<>();
        collectChildCategoryIds(categoryId, categoryIds);
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);

        Page<ProductDtoView> productDtoViews = productDtoViewRepository.findByCategoryId(categoryIds,pageable);

        return new PaginatedResponse<>(
                productDtoViews.getContent(),
                productDtoViews.getTotalPages(),
                productDtoViews.getNumber()+1,
                productDtoViews.getTotalElements()
        );
    }

    private Page<Product> searchProductByCategory(String search, Long categoryId, Pageable pageable) {
        List<Long> categoryIds = new ArrayList<>();
        collectChildCategoryIds(categoryId, categoryIds);

        // Tìm kiếm sản phẩm theo từ khóa và category
        return productRepository.findBySearchAndCategoryIds(search, categoryIds, pageable);
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


    public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        // Lấy danh sách tất cả category con, bao gồm cả category cha
        List<Long> categoryIds = new ArrayList<>();
        collectChildCategoryIds(categoryId, categoryIds);

        // Thực hiện phân trang dựa trên danh sách categoryIds
        return productRepository.findByCategoryIds(categoryIds, pageable);
    }

    // Hàm đệ quy để thu thập tất cả các category con (bao gồm cả category hiện tại)
    private void collectChildCategoryIds(Long categoryId, List<Long> categoryIds) {
        // Thêm category hiện tại vào danh sách
        categoryIds.add(categoryId);

        // Lấy các category con của category hiện tại
        List<Category> childCategories = categoryRepository.findByParentId(categoryId);

        // Đệ quy qua các category con
        for (Category child : childCategories) {
            collectChildCategoryIds(child.getId(), categoryIds);
        }
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




    @Transactional
    public Object getProductById(Long productId) {
        Product product=productRepository.findById(productId).orElseThrow(()->new RuntimeException("Khong tim thay san pham"));
        return product.mapToProductDtoWithDetails();
    }
}
