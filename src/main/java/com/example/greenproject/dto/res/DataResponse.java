package com.example.greenproject.dto.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataResponse extends BaseResponse{
    private Object data;
    public DataResponse(int code, String message, Object data){
        super(code,message);
        this.data = data;
    }
}
