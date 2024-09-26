package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateProductRequest;

import com.example.greenproject.dto.req.UpdateProductRequest;

import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.dto.res.ProductDto;
import com.example.greenproject.dto.res.ProductDtoView;
import com.example.greenproject.exception.NotFoundException;
import com.example.greenproject.model.Category;

import com.example.greenproject.model.Product;
import com.example.greenproject.model.ProductItem;
import com.example.greenproject.repository.CategoryRepository;
import com.example.greenproject.repository.ProductItemRepository;
import com.example.greenproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;
    private final CategoryRepository categoryRepository;
    public Object getAllProduct(Integer pageNum, Integer pageSize, String search,Long categoryId) {
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


        List<ProductDto> productDtos = products.getContent().stream().map(Product::mapToProductDto).toList();
        return new PaginatedResponse<>(
                productDtos,
                products.getTotalPages(),
                products.getNumber()+1,
                products.getTotalElements()
        );
    }

    private List<Product> sortProductByPrice(){
        // Sắp xếp sản phẩm theo giá
        List<ProductItem> productItems = productItemRepository.findAll();

        return productItems.stream().map(ProductItem::getProduct).toList();
    }

    public Object getAllRelatedProduct(Integer pageNum, Integer pageSize,Long categoryId){
        List<Long> categoryIds = new ArrayList<>();
        collectChildCategoryIds(categoryId, categoryIds);
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);

        Page<Product> products = productRepository.findByCategoryId(categoryId,pageable);

        List<ProductDtoView> productDtoViews = products.getContent()
                .stream()
                .map(Product::mapToProductDtoView)
                .toList();

        return new PaginatedResponse<>(
                productDtoViews,
                products.getTotalPages(),
                products.getNumber()+1,
                products.getTotalElements()
        );
    }

    private Page<Product> searchProductByCategory(String search, Long categoryId, Pageable pageable) {
        List<Long> categoryIds = new ArrayList<>();
        collectChildCategoryIds(categoryId, categoryIds);

        // Tìm kiếm sản phẩm theo từ khóa và category
        return productRepository.findBySearchAndCategoryIds(search, categoryIds, pageable);
    }

    public PaginatedResponse<ProductDtoView> getProductItemByTopSold(Integer pageNum,Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        // Lấy danh sách tất cả sản phẩm bán được nhiều nhất
        Page<Product> products= productRepository.findByTopSold(pageable);

        List<ProductDtoView> productDtoViews = products.stream().map(Product::mapToProductDtoView).toList();
        return new PaginatedResponse<>(
                productDtoViews,
                products.getTotalPages(),
                products.getNumber()+1,
                products.getTotalElements()
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
        Optional<Product> product=productRepository.findById(id);
        if(product.isPresent()){
            if(!product.get().getProductItems().isEmpty()){
                throw new RuntimeException("Sản phẩm này đang có nhiều sản phẩm chi tiet!");
            }

            if(!product.get().getImages().isEmpty()){
                throw new RuntimeException("Sản phẩm này đang có nhiều anh!");
            }

        }
       productRepository.deleteById(id);
    }


    public Object getAllProductViews(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        Page<Product> products = productRepository.findAll(pageable);
        return new PaginatedResponse<>(
                products.stream().map(Product::mapToProductDtoView).toList(),
                products.getTotalPages(),
                products.getNumber()+1,
                products.getTotalElements()
        );
    }

    public Object getProductById(Long productId) {
        Product product=productRepository.findById(productId).orElseThrow(()->new RuntimeException("Khong tim thay san pham"));
        return product.mapToProductDtoWithDetails();
    }
}
