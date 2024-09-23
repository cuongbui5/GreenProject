package com.example.greenproject.model.pk;

import com.example.greenproject.model.Product;
import com.example.greenproject.model.User;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Setter
@Getter
@EqualsAndHashCode
public class ReviewId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
