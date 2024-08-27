package com.example.greenproject.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse extends BaseResponse {
    private String urlPayment;

    public PaymentResponse(int code, String message,String urlPayment) {
        super(code, message);
        this.urlPayment = urlPayment;
    }
}
