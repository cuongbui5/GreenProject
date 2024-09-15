package com.example.greenproject.dto.req;

import com.example.greenproject.dto.req_abstract.AbstractVoucherRequest;
import com.example.greenproject.model.enums.VoucherType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateVoucherRequest extends AbstractVoucherRequest {

}
