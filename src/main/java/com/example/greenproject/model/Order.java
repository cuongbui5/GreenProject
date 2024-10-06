package com.example.greenproject.model;

import com.example.greenproject.dto.res.OrderDto;
import com.example.greenproject.dto.res.OrderDtoLazy;
import com.example.greenproject.model.enums.OrderStatus;
import com.example.greenproject.model.enums.VoucherType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    @JsonIgnore
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id",referencedColumnName = "id")
    @JsonIgnore
    private Contact contact;
    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Item> items=new ArrayList<>();
    private boolean isPaid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id",referencedColumnName = "id")
    @JsonIgnore
    private Voucher voucher;
    private Double productTotalCost;
    private Double shippingCost;
    private Double totalCost;
    private Double discountAmount=0.0;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    public OrderDto mapToOrderDto(){
        OrderDto dto = new OrderDto();
        dto.setId(id);
        dto.setContact(contact==null?null:contact.mapToContactDto());
        dto.setVoucher(voucher==null?null:voucher.mapToVoucherDto());
        dto.setItems(items.stream().map(Item::mapToItemDto).toList());
        dto.setPaid(isPaid);
        dto.setProductTotalCost(productTotalCost);
        dto.setShippingCost(shippingCost);
        dto.setTotalCost(totalCost);
        dto.setDiscountAmount(discountAmount);
        dto.setStatus(status);
        return dto;
    }

    public OrderDtoLazy mapToOrderDtoLazy(){
        OrderDtoLazy dto = new OrderDtoLazy();
        dto.setId(id);
        dto.setDiscountAmount(discountAmount);
        dto.setShippingCost(shippingCost);
        dto.setTotalCost(totalCost);
        dto.setProductTotalCost(productTotalCost);
        return dto;
    }

    public void calculateDiscountAmount(){
        if(voucher==null){
            return ;
        }

        if(voucher.getType()== VoucherType.FREE_SHIP){
            this.discountAmount=this.shippingCost;
            return;
        }

        if(voucher.getType()== VoucherType.DISCOUNT_AMOUNT){
            this.discountAmount=voucher.getValue();
            return;
        }

        this.discountAmount=voucher.getValue()*productTotalCost*0.01;
    }

    public void calculateProductTotalCost() {
        this.productTotalCost = items.stream()
                .mapToDouble(item -> item.getProductItem().getPrice() * item.getQuantity())
                .sum();
    }
    public void calculateShippingCost() {
        this.shippingCost = productTotalCost*0.1;
    }
    public void calculateTotalCost() {
        this.totalCost = (productTotalCost + shippingCost) - discountAmount;
    }
    public void calculateAllCosts() {
        calculateProductTotalCost();
        calculateShippingCost();
        calculateDiscountAmount();
        calculateTotalCost();
    }


}
