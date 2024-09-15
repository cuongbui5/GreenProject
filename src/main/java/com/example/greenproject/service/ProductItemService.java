package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateProductItemRequest;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.model.Product;
import com.example.greenproject.model.ProductItem;
import com.example.greenproject.model.Variation;
import com.example.greenproject.repository.ProductItemRepository;
import com.example.greenproject.repository.ProductRepository;
import com.example.greenproject.repository.VariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ProductItemService {
    private final ProductItemRepository productItemRepository;
    private final ProductRepository productRepository;
    private final VariationRepository variationRepository;

    public Object findAllProductItem(Integer pageNum, Integer pageSize){
        var productItems = (pageNum == null && pageSize == null) ?
                findAllProductItemList() : findAllProductItemPagination(pageNum,pageSize);

        if(productItems instanceof Page<ProductItem> temp){
            return new PaginatedResponse<>(
                    temp.getContent(),
                    temp.getTotalPages(),
                    temp.getNumber(),
                    temp.getTotalElements()
            );
        }
        return productItems;
    }

    private List<ProductItem> findAllProductItemList(){
        return productItemRepository.findAll();
    }

    private Page<ProductItem> findAllProductItemPagination(Integer pageNum, Integer pageSize){
        return productItemRepository.findAll(PageRequest.of(pageNum,pageSize));
    }

    public List<ProductItem> getAllProductItemByProductId(Long id){
        List<ProductItem> productItems = productItemRepository.findAll();

        List<ProductItem> filterProduct = productItems.stream()
                .filter(productItem -> productItem.getProduct().getId().equals(id))
                .toList();

        return productItems;
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
//    public List<ProductItem>  createProductItem(CreateProductItemRequest createProductItemRequest){
//        Random random = new Random();
//
//        Long productId = createProductItemRequest.getProductId();
//
//        //TODO must check product id exist or not
//        Product product = productRepository.findById(productId)
//                .orElseThrow(()->new RuntimeException("Not found product id" + productId));
//
//        Long categoryId = product.getCategory().getId();
//        List<Variation> variations = variationRepository.getAllVariationByCategoryId(categoryId);
//
//        int length = variations.size();
//        int count = 0;
//        List<ProductItem> productItems = new ArrayList<>();
//        for(var tempVariation : variations){
//            for(var tempOption : tempVariation.getVariationOptions()){
//                //TODO check if variation equals 1
//                ProductItem productItem = ProductItem
//                        .builder()
//                        .product(product)
//                        .price(random.nextDouble(1000000,100000000))
//                        .quantity(random.nextInt(-1,500))
//                        .build();
//                productItem.getVariationOptions().add(tempOption);
//                productItems.add(productItem);
//
//                //TODO if variation > 1 then ....
//                if(count > 0 && count != length) {
//                    List<ProductItem> tempProductItems = new ArrayList<>();
//                    for (var item : productItems) {
//                        productItem.getVariationOptions().addAll(item.getVariationOptions());
//                        productItems.add(productItem);
//                    }
//                }
//            }
//            count++;
//        }
//        return productItemRepository.saveAll(productItems);
//    }
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
}
