package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateProductItemRequest;
import com.example.greenproject.dto.req.UpdateProductItemRequest;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.dto.res.ProductItemDto;
import com.example.greenproject.dto.res.ProductItemDtoView;
import com.example.greenproject.exception.NotFoundException;
import com.example.greenproject.model.Product;
import com.example.greenproject.model.ProductItem;
import com.example.greenproject.model.VariationOption;
import com.example.greenproject.repository.ProductItemRepository;
import com.example.greenproject.repository.ProductRepository;
import com.example.greenproject.repository.VariationOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductItemService {
    private final ProductItemRepository productItemRepository;
    private final ProductRepository productRepository;
    private final VariationOptionRepository variationOptionRepository;
    public Object getAllProductItem(Integer pageNum, Integer pageSize, String search, Long productId) {
        if(pageNum==null || pageSize==null){
            return getAllProductItems();
        }
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        Page<ProductItem> productItems = null;
        
        if(search==null&&productId==null){
            productItems = productItemRepository.findAll(pageable);
        }

        if(search!=null){
            productItems= searchProductItem(search,pageable);
        }
        
        if(productId!=null){
            productItems = productItemRepository.findByProductId(productId,pageable);
        }

        List<ProductItemDto> productItemDtos = productItems.getContent().stream().map(ProductItem::mapToProductItemDto).toList();
        return new PaginatedResponse<>(
                productItemDtos,
                productItems.getTotalPages(),
                productItems.getNumber()+1,
                productItems.getTotalElements()
        );
    }

    public List<ProductItemDtoView> getProductItemByTopSold(Integer limit){
        List<ProductItem> productItems = productItemRepository.findAll();
        productItems.sort(Comparator.comparing(ProductItem::getSold).reversed());

        return productItems.stream().limit(limit).map(ProductItem::mapToProductItemDtoView).toList();
    }

    private Page<ProductItem> searchProductItem(String search, Pageable pageable) {
        return productItemRepository.findByProductNameContainingIgnoreCase(search, pageable);
    }


    public List<ProductItemDto> getAllProductItems(){
        return productItemRepository.findAll().stream().map(ProductItem::mapToProductItemDto).toList();
    }



    public ProductItem findProductItemById(Long id){
        return productItemRepository.findById(id).orElseThrow(()->new RuntimeException("Không tìm thấy sản phẩm với id: "+id));
    }



    public ProductItemDto createProductItem(CreateProductItemRequest createProductItemRequest){
        Optional<Product> productOptional = productRepository.findById(createProductItemRequest.getProductId());
        if(productOptional.isEmpty()){
            throw new NotFoundException("Khong tim thay san pham!");

        }
        Product product = productOptional.get();
        Set<VariationOption> newVariationOptions = new HashSet<>();
        createProductItemRequest.getProductConfig().forEach(i -> {
            VariationOption variationOption = variationOptionRepository.findById(i)
                    .orElseThrow(() -> new NotFoundException("Khong tim thay option id:" + i));
            newVariationOptions.add(variationOption);
        });
        for (ProductItem productItem : product.getProductItems()) {
            if (productItem.getVariationOptions().equals(newVariationOptions)) {
                throw new IllegalArgumentException("ProductItem này đã tồn tại!");
            }
        }

        ProductItem productItem = new ProductItem();
        productItem.setProduct(product);
        productItem.setQuantity(createProductItemRequest.getQuantity());
        productItem.setPrice(createProductItemRequest.getPrice());
        productItem.setVariationOptions(newVariationOptions);

        return productItemRepository.save(productItem).mapToProductItemDto();




    }

    public ProductItemDto updateProductItem(Long productItemId, UpdateProductItemRequest updateProductItemRequest){
        ProductItem productItem = productItemRepository
                .findById(productItemId)
                .orElseThrow(()->new NotFoundException("Not found"));
        if(updateProductItemRequest.getPrice()!=null&& !productItem.getPrice().equals(updateProductItemRequest.getPrice())){
            productItem.setPrice(updateProductItemRequest.getPrice());
        }
        if(updateProductItemRequest.getQuantity() != null && !Objects.equals(productItem.getQuantity(), updateProductItemRequest.getQuantity())){
            productItem.setQuantity(updateProductItemRequest.getQuantity());
        }
        Set<VariationOption> variationOptions= productItem.getVariationOptions();
        variationOptions.clear();

        for (int i=0;i<=updateProductItemRequest.getProductConfig().size()-1;i++){
            Long optionId=updateProductItemRequest.getProductConfig().get(i);

            VariationOption v=variationOptionRepository
                    .findById(optionId)
                    .orElseThrow(()->new NotFoundException("Khong tim thay option"));
            variationOptions.add(v);

        }



        return productItemRepository.save(productItem).mapToProductItemDto();
    }

    public void deleteProductItem(Long id){
        productItemRepository.deleteById(id);
    }
}
