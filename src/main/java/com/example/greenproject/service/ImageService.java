package com.example.greenproject.service;

import com.cloudinary.Cloudinary;
import com.example.greenproject.dto.req.CreateImageRequest;
import com.example.greenproject.dto.req.CreateImagesRequest;
import com.example.greenproject.dto.req.UpdateImageRequest;
import com.example.greenproject.dto.res.ImageDto;
import com.example.greenproject.model.Image;
import com.example.greenproject.model.Product;
import com.example.greenproject.repository.ImageRepository;
import com.example.greenproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final Cloudinary cloudinary;
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
    }

    public List<ImageDto> getAllImageByProductId(Long id){
        return imageRepository.getAllByProductId(id).stream().map(Image::mapToImageDto).toList();
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



    public void deleteImageById(Long id) {
        imageRepository.deleteById(id);
    }

    public Image createImage(CreateImageRequest createImageRequest) {
        Product product = productRepository.findById(createImageRequest.getProductId())
                .orElseThrow(()->new RuntimeException("Không tìm thấy thông tin sản phẩm với id: "+createImageRequest.getProductId()));
        Image image = new Image();
        image.setUrl(createImageRequest.getUrl());
        image.setProduct(product);
        return imageRepository.save(image);

    }

    public List<ImageDto> createImages(CreateImagesRequest createImagesRequest) {
        Product product = productRepository.findById(createImagesRequest.getProductId())
                .orElseThrow(()->new RuntimeException("Không tìm thấy thông tin sản phẩm với id: "+createImagesRequest.getProductId()));
        List<ImageDto> imageDtos=new ArrayList<>();
        createImagesRequest.getImages().forEach(url->{
            Image image = new Image();
            image.setUrl(url);
            image.setProduct(product);
            imageDtos.add(imageRepository.save(image).mapToImageDto());
        });
        return imageDtos;
    }

    public ImageDto uploadFile(MultipartFile file, String productId) throws IOException {
        Product product = productRepository.findById(Long.parseLong(productId))
                .orElseThrow(()->new RuntimeException("Không tìm thấy thông tin sản phẩm với id: "+productId));
        Image image = new Image();
        String url=uploadFile(file);
        System.out.println("url: "+url);
        image.setUrl(url);
        image.setProduct(product);
        return imageRepository.save(image).mapToImageDto();
    }
}
