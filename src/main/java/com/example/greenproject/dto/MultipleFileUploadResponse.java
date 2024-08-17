package com.example.greenproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MultipleFileUploadResponse {
    private String status;
    private List<String> url;
}
