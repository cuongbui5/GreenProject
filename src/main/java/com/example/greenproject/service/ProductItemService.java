package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateProductItemRequest;
import com.example.greenproject.dto.req.CreateProductRequest;
import com.example.greenproject.dto.req.UpdateProductItemRequest;
import com.example.greenproject.dto.req.UpdateProductRequest;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.dto.res.ProductDto;
import com.example.greenproject.dto.res.ProductItemDto;
import com.example.greenproject.exception.NotFoundException;
import com.example.greenproject.model.Category;
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
    public Object getAllProductItem(Integer pageNum, Integer pageSize, String search) {
        if(pageNum==null || pageSize==null){
            return getAllProductItems();
        }
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        Page<ProductItem> productItems;

        if(search!=null){
            productItems= searchProductItem(search,pageable);
        }else {

            productItems = productItemRepository.findAll(pageable);
        }

        List<ProductItemDto> productItemDtos = productItems.getContent().stream().map(ProductItem::mapToProductItemDto).toList();
        return new PaginatedResponse<>(
                productItemDtos,
                productItems.getTotalPages(),
                productItems.getNumber()+1,
                productItems.getTotalElements()
        );
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
        Optional<Product> product = productRepository.findById(createProductItemRequest.getProductId());
        if(product.isEmpty()){
            throw new NotFoundException("Khong tim thay san pham!");

        }

        ProductItem productItem=new ProductItem();
        productItem.setProduct(product.get());
        productItem.setQuantity(createProductItemRequest.getQuantity());
        productItem.setPrice(createProductItemRequest.getPrice());
        if(!createProductItemRequest.getProductConfig().isEmpty()){
            Set<VariationOption> variationOptions=new HashSet<>();
            createProductItemRequest.getProductConfig().forEach(i->{
                VariationOption variationOption=variationOptionRepository.findById(i)
                        .orElseThrow(()->new NotFoundException("Khong tim thay option id:"+i));
                variationOptions.add(variationOption);
            });
            productItem.setVariationOptions(variationOptions);
        }
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
