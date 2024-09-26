package com.example.greenproject.dto.req;


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
public class CreateVoucherRequest  {
    @NotNull(message = "Tên không được để trống")
    @Size(min = 3, max = 100, message = "Tên phải có độ dài từ 3 đến 100 ký tự")
    private String name;

    @Size(max = 255, message = "Mô tả không được dài quá 255 ký tự")
    private String description;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
    private Integer quantity;

    @NotNull(message = "Điểm yêu cầu không được để trống")
    @Min(value = 0, message = "Điểm yêu cầu phải lớn hơn hoặc bằng 0")
    private Integer pointsRequired;

    private Long version;

    @NotNull(message = "Loại voucher không được để trống")
    private VoucherType type;

    @NotNull(message = "Giá trị không được để trống")
    @Min(value = 0, message = "Giá trị phải lớn hơn hoặc bằng 0")
    private double value;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDateTime startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @Future(message = "Ngày kết thúc phải là ngày trong tương lai")
    private LocalDateTime endDate;
}
