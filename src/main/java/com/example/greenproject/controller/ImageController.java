package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateImageRequest;
import com.example.greenproject.dto.req.UpdateImageRequest;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.model.Image;
import com.example.greenproject.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getImagesByProductId(@PathVariable("id") Long id){
        List<Image> images = imageService.getAllImageByProductId(id);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Successfully retriever image list by product id " + id,
                images));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createImage(@RequestBody CreateImageRequest createImageRequest){
        Image saveImage = imageService.addImageToProduct(createImageRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse(
                HttpStatus.CREATED.value(),
                "Successfully create and add new image to image list in product " + createImageRequest.getProductId(),
                saveImage));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable("id") Long id){
        imageService.deleteImageFromProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Successfully delete image id " + id,
                null));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateImage(@PathVariable("id") Long id,@RequestBody UpdateImageRequest updateImageRequest){
        Image saveImage = imageService.updateImageToProduct(id,updateImageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                "Successfully update image id " + id,
                saveImage));
    }
}
