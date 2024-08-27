package com.example.greenproject.model;

import com.example.greenproject.model.enums.VoucherType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "_voucher")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Voucher extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;// Tên của voucher
    private String description;
    private Integer quantity;
    private Integer pointsRequired;

    @Enumerated(EnumType.STRING)
    private VoucherType type; // Loại voucher (FREE_SHIP, DISCOUNT_PERCENTAGE, ...)

    private Double value; // Giá trị của voucher (ví dụ: 10%, 50.000 VND,...)

    private LocalDateTime startDate; // Ngày bắt đầu hiệu lực

    private LocalDateTime endDate; // Ngày kết thúc hiệu lực


    private Boolean isActive;// Trạng thái của voucher (còn hiệu lực hay không)
}
