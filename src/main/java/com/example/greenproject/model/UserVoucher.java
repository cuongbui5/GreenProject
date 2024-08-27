package com.example.greenproject.model;

import com.example.greenproject.model.pk.UserVoucherId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "_user_voucher")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserVoucher extends BaseEntity{
    @Id
    private UserVoucherId id;
    private Integer quantity;
}
