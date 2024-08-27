package com.example.greenproject.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BaseResponse {
    private int code;
    private String message;
}
