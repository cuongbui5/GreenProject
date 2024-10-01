package com.example.greenproject.model;

import com.example.greenproject.dto.res.ItemDto;
import com.example.greenproject.model.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Table(name = "_item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_item_id",referencedColumnName = "id")
    private ProductItem productItem;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id",referencedColumnName = "id")
    @JsonIgnore
    private Cart cart;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    @JsonIgnore
    private Order order;
    private int quantity;
    private Double totalPrice;
    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    public ItemDto mapToItemDto(){
        ItemDto itemDto = new ItemDto();
        itemDto.setId(id);
        itemDto.setProductItem(productItem.mapToProductItemDto());
        itemDto.setQuantity(quantity);
        itemDto.setTotalPrice(totalPrice);
        itemDto.setStatus(status);
        itemDto.setCreatedAt(getCreatedAt());
        itemDto.setUpdatedAt(getUpdatedAt());
        return itemDto;
    }

    public void calculateTotalPrice(){
        this.totalPrice=this.productItem.getPrice()*this.getQuantity();
    }
}
