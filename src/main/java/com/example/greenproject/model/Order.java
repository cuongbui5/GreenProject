package com.example.greenproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "_order")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "contact_id",referencedColumnName = "id")
    private Contact contact;
    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER)
    private List<Item> items=new ArrayList<>();
    private boolean isPaid;
    @OneToOne
    @JoinColumn(name = "voucher_id",referencedColumnName = "id")
    private Voucher voucher;
    private Double shippingCost;
    private Double totalCost;


}
