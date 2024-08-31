package com.example.greenproject.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends BaseResponse{
    private String type;
    public ErrorResponse(int code, String message) {
        super(code, message);
        this.type = "error";
    }
}
