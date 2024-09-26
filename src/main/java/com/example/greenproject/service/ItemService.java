package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateCartItemRequest;
import com.example.greenproject.dto.req.CreateOrderItemRequest;
import com.example.greenproject.dto.req.UpdateCartQuantity;
import com.example.greenproject.model.Cart;
import com.example.greenproject.model.Item;
import com.example.greenproject.model.Order;
import com.example.greenproject.model.ProductItem;
import com.example.greenproject.model.enums.ItemStatus;
import com.example.greenproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductItemRepository productItemRepository;
    public Item createCartItem(CreateCartItemRequest createCartItemRequest) {
        Optional<ProductItem> productItem=productItemRepository.findById(createCartItemRequest.getProductItemId());
        if(productItem.isEmpty()){
            throw new RuntimeException("not find productItem");
        }
        Cart cart=cartService.getOrCreateCart();
        Item item = new Item();
        item.setCart(cart);
        item.setStatus(ItemStatus.CART_ITEM);
        item.setQuantity(createCartItemRequest.getQuantity());
        item.setProductItem(productItem.get());
        item.setTotalPrice(productItem.get().getPrice()*createCartItemRequest.getQuantity());
        return itemRepository.save(item);
    }
    public Item createOrderItem(CreateOrderItemRequest createOrderItemRequest) {
        Item item = new Item();
        Optional<Order> orderOptional= orderRepository.findById(createOrderItemRequest.getOrderId());
        if(orderOptional.isEmpty()){
            throw new RuntimeException("not find order");
        }

        Optional<ProductItem> productItem=productItemRepository.findById(createOrderItemRequest.getProductItemId());
        if(productItem.isEmpty()){
            throw new RuntimeException("not find productItem");
        }
        item.setOrder(orderOptional.get());
        item.setStatus(ItemStatus.ORDER_ITEM);
        item.setQuantity(createOrderItemRequest.getQuantity());
        item.setProductItem(productItem.get());
        item.setTotalPrice(productItem.get().getPrice()*createOrderItemRequest.getQuantity());

        return itemRepository.save(item);
    }
    public void deleteCartItem(Long id){
        itemRepository.deleteById(id);
    }

    public List<Item> getAllCartItem() {
        Cart cart=cartService.getOrCreateCart();
        return itemRepository.findByCart(cart);
    }


    public Object updateCartQuantity(UpdateCartQuantity updateCartQuantity, Long itemId) {
        Item item=itemRepository.findById(itemId).orElseThrow(()->new RuntimeException("not find item"));
        item.setQuantity(updateCartQuantity.getQuantity());
        return itemRepository.save(item);
    }

    public void deleteItemById(Long itemId) {
        orderRepository.deleteById(itemId);
    }
}
