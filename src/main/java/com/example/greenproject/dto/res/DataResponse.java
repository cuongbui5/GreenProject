package com.example.greenproject.dto.res;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class DataResponse extends BaseResponse {
    private Object data;
    public DataResponse(int code, String message,Object data) {
        super(code, message);
        this.data = data;
       
    }
}
