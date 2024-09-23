package com.example.greenproject.model.pk;

import com.example.greenproject.model.Product;
import com.example.greenproject.model.User;
import com.example.greenproject.model.Voucher;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Setter
@Getter
@EqualsAndHashCode
public class UserVoucherId {
    @ManyToOne
    @JoinColumn(name = "voucher_id", referencedColumnName = "id")
    private Voucher voucher;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
