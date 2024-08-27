package com.example.greenproject.controller;

import com.example.greenproject.dto.res.FileUploadResponse;
import com.example.greenproject.dto.res.MultipleFileUploadResponse;
import com.example.greenproject.service.FileUploadService;
import com.example.greenproject.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/upload")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileUploadService fileUploadService;
    @PostMapping("/image")
    public ResponseEntity<?> uploadImage( @RequestPart("image") final MultipartFile file) throws IOException {
        String url= fileUploadService.uploadFile(file);
        if(!url.isBlank()) {
            return ResponseEntity.ok(new FileUploadResponse(Constants.STATUS_OK,url));
        }else {
            return ResponseEntity.ok(new FileUploadResponse(Constants.STATUS_FAIL,url));
        }


    }
    @PostMapping("/images")
    public ResponseEntity<?> uploadImages(@RequestPart("images") final MultipartFile[] files) throws IOException {
        List<String> urls= fileUploadService.uploadFiles(files);
        if(!urls.isEmpty()) {
            return ResponseEntity.ok(new MultipleFileUploadResponse(Constants.STATUS_OK,urls));
        }else {
            return ResponseEntity.ok(new MultipleFileUploadResponse(Constants.STATUS_FAIL,urls));
        }


    }
}
