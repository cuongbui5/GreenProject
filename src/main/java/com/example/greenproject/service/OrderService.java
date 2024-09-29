package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateOrderRequest;
import com.example.greenproject.dto.req.CreateOrderRequestWithProductItem;
import com.example.greenproject.dto.req.CreatePaymentRequest;
import com.example.greenproject.dto.req.UpdateOrderRequest;
import com.example.greenproject.exception.NotFoundException;
import com.example.greenproject.model.*;
import com.example.greenproject.model.enums.ItemStatus;
import com.example.greenproject.model.enums.OrderStatus;
import com.example.greenproject.repository.*;
import com.example.greenproject.security.UserInfo;
import com.example.greenproject.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final CartService cartService;
    private final ProductItemRepository productItemRepository;
    private final PaymentAccountRepository paymentAccountRepository;
    private final UserService userService;
    private final ItemService itemService;
    @Transactional
    public Order createOrderByCart() {
        Cart cart=cartService.getOrCreateCart();
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create order from an empty cart");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setItems(new ArrayList<>());


        double totalCost = 0.0;
        for (Item item : cart.getItems()) {
            totalCost += item.getTotalPrice();
            order.getItems().add(item);
        }


        double shippingCost = totalCost * 0.1;
        double total = totalCost + shippingCost;
        order.setProductTotalCost(totalCost);
        order.setShippingCost(shippingCost);
        order.setTotalCost(total);
        order.setPaid(false);


        Order savedOrder = orderRepository.save(order);


        for (Item item : cart.getItems()) {
            item.setOrder(savedOrder);
            item.setStatus(ItemStatus.ORDER_ITEM);
        }


        itemRepository.saveAll(cart.getItems());

        return savedOrder;

    }

    public Order createEmptyOrder() {
        Order order = new Order();
        order.setStatus(OrderStatus.INIT);
        order.setUser(userService.getUserInfo());
        order.setProductTotalCost(0.0);
        order.setShippingCost(0.0);
        order.setTotalCost(0.0);
        order.setPaid(false);
        return orderRepository.save(order);
    }

    public Order updateOrder(long orderId, UpdateOrderRequest updateOrderRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (!order.isPaid()) {
            throw new IllegalStateException("Order is not paid");
        }

        OrderStatus newStatus = updateOrderRequest.getOrderStatus();
        OrderStatus currentStatus = order.getStatus();

        switch (newStatus) {
            case CANCELED:
                if (currentStatus == OrderStatus.PENDING) {
                    order.setStatus(OrderStatus.CANCELED);
                    break;
                }
                break;

            case PROCESSING:
                if (currentStatus == OrderStatus.PENDING) {
                    order.setStatus(OrderStatus.PROCESSING);
                    break;
                }
                break;

            case SHIPPED:
                if (currentStatus == OrderStatus.PROCESSING) {
                    order.setStatus(OrderStatus.SHIPPED);
                    break;
                }
                break;

            case DELIVERED:
                if (currentStatus == OrderStatus.SHIPPED) {
                    order.setStatus(OrderStatus.DELIVERED);
                    break;
                }
                break;

            case RETURNED:
                if (currentStatus == OrderStatus.DELIVERED) {
                    order.setStatus(OrderStatus.RETURNED);
                    break;
                }
                break;

            default:
                throw new RuntimeException("Invalid order status");
        }

        return orderRepository.save(order);

    }
    @Transactional
    public Order pay(CreatePaymentRequest createPaymentRequest) {
        Order order = orderRepository.findById(createPaymentRequest.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + createPaymentRequest.getOrderId()));


        PaymentAccount paymentAccount=paymentAccountRepository.findById(createPaymentRequest.getPaymentAccountId())
                .orElseThrow(() -> new NotFoundException("không tim thay tai khoan thanh toan "));


        if(!Objects.equals(paymentAccount.getPin(), createPaymentRequest.getPin())) {
            throw new IllegalStateException("Sai ma pin!");
        }
        if(order.getContact()==null){
            throw new RuntimeException("Vui long them dia chi giao hang");
        }
        if(paymentAccount.getBalance()<order.getTotalCost()){
            throw new RuntimeException("Tai khoan nay khong du tien thanh toan!Vui long chon tai khoan khac!");
        }

        boolean isCancelled = order.getItems().stream()
                .anyMatch(item -> item.getProductItem().getQuantity() < item.getQuantity());

        if (isCancelled) {
            order.setStatus(OrderStatus.CANCELED);
            return orderRepository.save(order);
        }

        for (Item item : order.getItems()) {
            ProductItem productItem = productItemRepository.findById(item.getProductItem().getId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            if(productItem.getQuantity()==0){
                throw new RuntimeException("San pham da het hang");
            }

            if (productItem.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("So luong san pham trong kho khong du");

            }


            productItem.setQuantity(productItem.getQuantity() - item.getQuantity());
            productItem.setSold(productItem.getSold() + item.getQuantity());
        }
        paymentAccount.setBalance(paymentAccount.getBalance()-order.getTotalCost());

        User user=paymentAccount.getUser();
        int pointsToAdd = (order.getTotalCost() >= 500000) ? 5000 : (order.getTotalCost() >= 100000) ? 1000 : 0;
        if (pointsToAdd > 0) {
            user.setPoints(user.getPoints() + pointsToAdd);

        }


        order.setPaid(true);
        order.setStatus(OrderStatus.PENDING);
        return order;
    }

    public Object createOrderByProductItem(CreateOrderRequestWithProductItem createOrderRequestWithProductItem) {
        Item item = itemService.createItem(createOrderRequestWithProductItem.getProductItemId(), createOrderRequestWithProductItem.getQuantity());
        item.setStatus(ItemStatus.ORDER_ITEM);

        // Tính toán tổng giá
        double productTotalCost = item.getTotalPrice();
        double shippingCost = productTotalCost * 0.1;
        double totalCost = productTotalCost + shippingCost;

        // Tạo Order
        Order order = new Order();
        order.setProductTotalCost(productTotalCost);
        order.setShippingCost(shippingCost);
        order.setTotalCost(totalCost);
        order.setPaid(false);
        order.setStatus(OrderStatus.INIT);

        // Lưu Order và Item
        order = orderRepository.save(order);
        item.setOrder(order); // Gán item cho order
        itemRepository.save(item); // Lưu item sau khi đã gán order

        return order;
    }
}
