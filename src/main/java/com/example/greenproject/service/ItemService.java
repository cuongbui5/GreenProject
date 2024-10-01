package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateCartItemRequest;
import com.example.greenproject.dto.req.CreateOrderItemRequest;
import com.example.greenproject.dto.req.UpdateCartQuantity;
import com.example.greenproject.dto.res.ItemDto;
import com.example.greenproject.model.Cart;
import com.example.greenproject.model.Item;
import com.example.greenproject.model.Order;
import com.example.greenproject.model.ProductItem;
import com.example.greenproject.model.enums.ItemStatus;
import com.example.greenproject.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductItemRepository productItemRepository;

    public void createCartItem(CreateCartItemRequest createCartItemRequest) {
        ProductItem productItem=productItemRepository.findById(createCartItemRequest.getProductItemId())
                .orElseThrow(()-> new RuntimeException("not find productItem"));

        Cart cart=cartService.getOrCreateCart();
        List<Item> items= itemRepository.findByCart(cart);
        if(!items.isEmpty()){
            for (Item item : items) {
                if(Objects.equals(item.getProductItem().getId(), productItem.getId())){
                    throw new RuntimeException("san pham da co trong gio hang");
                }
            }

        }

        Item item = new Item();
        item.setCart(cart);
        item.setStatus(ItemStatus.CART_ITEM);
        item.setQuantity(createCartItemRequest.getQuantity());
        item.setProductItem(productItem);
        item.setTotalPrice(productItem.getPrice()*createCartItemRequest.getQuantity());
        itemRepository.save(item);
    }

    public Item createItem(Long productItemId, Integer quantity) {
        ProductItem productItem = productItemRepository.findById(productItemId)
                .orElseThrow(() -> new RuntimeException("Product item not found"));
        Item item=new Item();;
        item.setProductItem(productItem);
        item.setQuantity(quantity);
        item.calculateTotalPrice();
        return itemRepository.save(item);


    }

    /*public void createOrderItem(CreateOrderItemRequest createOrderItemRequest) {
        Order order = orderRepository.findById(createOrderItemRequest.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        ProductItem productItem = productItemRepository.findById(createOrderItemRequest.getProductItemId())
                .orElseThrow(() -> new RuntimeException("Product item not found"));

        // Tính toán giá
        double totalPriceProduct = calculateTotalPrice(productItem.getPrice(), createOrderItemRequest.getQuantity());
        double shippingPrice = calculateShippingPrice(totalPriceProduct);
        double finalPrice = calculateFinalPrice(totalPriceProduct, shippingPrice);

        // Tạo Item
        Item item = new Item();
        item.setOrder(order);
        item.setStatus(ItemStatus.ORDER_ITEM);
        item.setQuantity(createOrderItemRequest.getQuantity());
        item.setProductItem(productItem);
        item.setTotalPrice(totalPriceProduct);
        itemRepository.save(item);

        // Cộng dồn giá trị cho Order
        order.setShippingCost(order.getShippingCost() + shippingPrice);
        order.setProductTotalCost(order.getProductTotalCost() + totalPriceProduct);
        order.setTotalCost(order.getTotalCost() + finalPrice);
        orderRepository.save(order);
    }*/



    public void deleteCartItem(Long id){
        itemRepository.deleteById(id);
    }

    @Transactional
    public List<ItemDto> getAllCartItem() {
        Cart cart=cartService.getOrCreateCart();
        List<ItemDto> list = itemRepository.findByCart(cart).stream().map(Item::mapToItemDto).toList();
        return list;
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
