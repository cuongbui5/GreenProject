package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateImageRequest;
import com.example.greenproject.dto.req.UpdateImageRequest;
import com.example.greenproject.model.Image;
import com.example.greenproject.model.Product;
import com.example.greenproject.repository.ImageRepository;
import com.example.greenproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;

    public List<Image> getAllImageByProductId(Long id){
        return imageRepository.getAllByProductId(id);
    }

    public Image addImageToProduct(CreateImageRequest createImageRequest){
        Long productId = createImageRequest.getProductId();
        String imageUrl = createImageRequest.getImageUrl();
        Product existOrNotProduct = productRepository.findById(productId).orElseThrow();

        existOrNotProduct.addImage(Image.builder().url(imageUrl).build());
        Product saveProduct = productRepository.save(existOrNotProduct);

        return saveProduct.getImages()
                .stream()
                .filter(image -> image.getUrl().equals(createImageRequest.getImageUrl()))
                .findFirst()
                .orElseThrow();
    }

    public Image updateImageToProduct(Long imageId, UpdateImageRequest imageRequest){
        Image existOrNotImage = imageRepository.findById(imageId).orElseThrow();

        if(imageRequest.getImageUrl() != null){
            existOrNotImage.setUrl(imageRequest.getImageUrl());
        }

        if(imageRequest.getProductId() != null){
            Product product = productRepository.findById(imageRequest.getProductId()).orElseThrow(()-> new RuntimeException("Not found product"));
            existOrNotImage.setProduct(product);
        }
        return imageRepository.save(existOrNotImage);
    }

    public void deleteImageFromProduct(Long id){
        Image existOrNotImage = imageRepository.findById(id).orElseThrow();
        existOrNotImage.setProduct(null);
        imageRepository.delete(existOrNotImage);
    }
}
