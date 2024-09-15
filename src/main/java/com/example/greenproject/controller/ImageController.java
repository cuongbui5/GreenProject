package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateImageRequest;
import com.example.greenproject.dto.req.CreateImagesRequest;
import com.example.greenproject.dto.req.UpdateImageRequest;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.model.Image;
import com.example.greenproject.service.ImageService;
import com.example.greenproject.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestPart("image") final MultipartFile file, @RequestPart("productId") final String productId) throws IOException {
        System.out.println(productId);
        return ResponseEntity.ok(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                imageService.uploadFile(file,productId)
        ));

    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getImagesByProductId(@PathVariable("productId") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                imageService.getAllImageByProductId(id)
        ));

    }

    @PostMapping("/create")
    public ResponseEntity<?> createImage(@RequestBody CreateImageRequest createImageRequest){
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                imageService.createImage(createImageRequest)
        ));
    }

    @PostMapping("/createImages")
    public ResponseEntity<?> createImage(@RequestBody CreateImagesRequest createImagesRequest){
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                imageService.createImages(createImagesRequest)
        ));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable("id") Long id){
        imageService.deleteImageById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                null));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateImage(@PathVariable("id") Long id,@RequestBody UpdateImageRequest updateImageRequest){
        Image saveImage = imageService.updateImageToProduct(id,updateImageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(saveImage);
    }
}
