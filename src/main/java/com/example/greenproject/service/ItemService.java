package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateCartItemRequest;
import com.example.greenproject.model.Cart;
import com.example.greenproject.model.Item;
import com.example.greenproject.model.enums.ItemStatus;
import com.example.greenproject.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final CartService cartService;
    public Item createCartItem(CreateCartItemRequest createCartItemRequest) {
        Item item = new Item();
        Optional<Cart> cartOptional= cartService.getCartById(createCartItemRequest.getCartId());
        if(cartOptional.isEmpty()){
            throw new RuntimeException("not find cart");
        }
        
        item.setCart(cartOptional.get());
        item.setStatus(ItemStatus.CART_ITEM);
        item.setQuantity(createCartItemRequest.getQuantity());




        return itemRepository.save(item);
    }

}
