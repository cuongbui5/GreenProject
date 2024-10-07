package com.example.greenproject.service;

import com.example.greenproject.dto.req.*;
import com.example.greenproject.dto.res.OrderDto;
import com.example.greenproject.dto.res.OrderDtoLazy;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.exception.NotFoundException;
import com.example.greenproject.model.*;
import com.example.greenproject.model.enums.ItemStatus;
import com.example.greenproject.model.enums.OrderStatus;
import com.example.greenproject.model.enums.VoucherType;
import com.example.greenproject.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductItemRepository productItemRepository;
    private final PaymentAccountRepository paymentAccountRepository;
    private final UserService userService;
    private final VoucherService voucherService;
    private final ContactRepository contactRepository;
    private final VoucherRepository voucherRepository;

    @Transactional
    public OrderDto createOrderByCart() {
        Cart cart=cartService.getOrCreateCart();
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create order from an empty cart");
        }

        Order order = new Order();
        order.setStatus(OrderStatus.INIT);
        for (Item item : cart.getItems()) {
            item.setStatus(ItemStatus.ORDER_ITEM);
            item.setCart(null);
            order.getItems().add(item);
            item.setOrder(order);
        }
        order.setUser(cart.getUser());
        order.setContact(null);
        order.setVoucher(null);
        order.setPaid(false);
        order.calculateAllCosts();
        return orderRepository.save(order).mapToOrderDto();
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }


    @Transactional
    public Order updateOrderStatus(long orderId, UpdateOrderStatusRequest updateOrderStatusRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (!order.isPaid()) {
            throw new IllegalStateException("Order is not paid");
        }

        OrderStatus newStatus = updateOrderStatusRequest.getOrderStatus();
        OrderStatus currentStatus = order.getStatus();

        switch (newStatus) {
            case CANCELED:
                if (currentStatus == OrderStatus.PENDING) {
                    order.setStatus(OrderStatus.CANCELED);
                    processRefund(order);
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
                    processRefund(order);
                    break;
                }
                break;

            default:
                throw new RuntimeException("Invalid order status");
        }

        return orderRepository.save(order);

    }

    @Transactional
    public void createPayment(CreatePaymentRequest createPaymentRequest) {
        Order order = orderRepository.findById(createPaymentRequest.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + createPaymentRequest.getOrderId()));


        PaymentAccount paymentAccount=paymentAccountRepository.findById(createPaymentRequest.getPaymentAccountId())
                .orElseThrow(() -> new NotFoundException("không tim thay tai khoan thanh toan "));


        if(!Objects.equals(paymentAccount.getPinCode(), createPaymentRequest.getPinCode())) {
            throw new IllegalStateException("Sai ma pin!");
        }
        if(order.getContact()==null){
            throw new RuntimeException("Vui long them dia chi giao hang");
        }
        if(paymentAccount.getBalance()<order.getTotalCost()){
            throw new RuntimeException("Tai khoan nay khong du tien thanh toan!Vui long chon tai khoan khac!");
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
        order.setPaymentAccount(paymentAccount);
        orderRepository.save(order);
        if(order.getVoucher()!=null){
            voucherService.deleteVoucherAfterPaymentSuccess(order.getVoucher().getId(), user.getId());
        }

    }

    @Transactional
    public OrderDto createOrderByProductItem(CreateOrderRequestWithProductItem createOrderRequestWithProductItem) {
        ProductItem productItem = productItemRepository.findById(createOrderRequestWithProductItem.getProductItemId())
                .orElseThrow(() -> new RuntimeException("Product item not found"));
        Item item=new Item();
        item.setProductItem(productItem);
        item.setQuantity(createOrderRequestWithProductItem.getQuantity());
        item.calculateTotalPrice();
        item.setStatus(ItemStatus.ORDER_ITEM);
        User user=userService.getUserByUserInfo();
        // Tạo Order
        Order order = new Order();
        order.setPaid(false);
        order.setStatus(OrderStatus.INIT);
        order.setUser(user);
        order.setContact(null);
        order.setVoucher(null);
        order.getItems().add(item);
        item.setOrder(order);
        order.calculateAllCosts();
        return orderRepository.save(order).mapToOrderDto();
    }

    @Transactional
    public void processRefund(Order order) {

        // Kiểm tra trạng thái đơn hàng
        if (!order.isPaid() || !order.getStatus().equals(OrderStatus.DELIVERED)) {
            throw new RuntimeException("Đơn hàng không đủ điều kiện để trả hàng.");
        }

        // Kiểm tra thời gian cập nhật
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        if (order.getUpdatedAt() == null ||
                order.getUpdatedAt().isBefore(currentDateTime.minusDays(15))) {
            throw new RuntimeException("Đơn hàng đã quá thời gian hoàn tiền (15 ngày).");
        }

        // Hoàn tiền
        PaymentAccount paymentAccount = order.getPaymentAccount();
        paymentAccount.setBalance(paymentAccount.getBalance() + order.getTotalCost());

        // Cập nhật trạng thái đơn hàng


        // Cập nhật lại sản phẩm trong kho
        for (Item item : order.getItems()) {
            ProductItem productItem = productItemRepository.findById(item.getProductItem().getId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            productItem.setQuantity(productItem.getQuantity() + item.getQuantity());
            productItem.setSold(productItem.getSold() - item.getQuantity());
        }
        order.setStatus(OrderStatus.RETURNED);
        //order.setPaid(false); // Đánh dấu là chưa thanh toán
        orderRepository.save(order);


    }

    @Transactional
    public OrderDto getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found with ID: " + id)).mapToOrderDto();
    }

    public void updateContactToOrder(UpdateContactOrderRequest updateContactOrderRequest) {
        Order order=orderRepository.findById(updateContactOrderRequest.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + updateContactOrderRequest.getOrderId()));
        Contact contact=contactRepository.findById(updateContactOrderRequest.getContactId())
                .orElseThrow(() -> new NotFoundException("Contact not found"));

        order.setContact(contact);

        orderRepository.save(order);
    }

    public OrderDtoLazy updateVoucherToOrder(UpdateVoucherOrderRequest updateVoucherOrderRequest) {
        Order order=orderRepository.findById(updateVoucherOrderRequest.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + updateVoucherOrderRequest.getOrderId()));
        Voucher voucher=voucherRepository.findById(updateVoucherOrderRequest.getVoucherId())
                .orElseThrow(() -> new NotFoundException("Voucher not found"));
        order.setVoucher(voucher);
        order.calculateAllCosts();

        return orderRepository.save(order).mapToOrderDtoLazy();
    }

    @Transactional
    public List<OrderDto> getAllOrderByStatus(OrderStatus orderStatus) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        return orderRepository.findByStatus(orderStatus,sort).stream().map(Order::mapToOrderDto).toList();
    }
    @Transactional
    public Object getAllOrder(Integer pageNum, Integer pageSize, OrderStatus orderStatus) {
        Pageable pageable = PageRequest.of(pageNum-1, pageSize, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<Order> orders = orderRepository.findByStatus(orderStatus,pageable);
        return new PaginatedResponse<>(
                orders.getContent().stream().map(Order::mapToOrderDtoLazy).toList(),
                orders.getTotalPages(),
                orders.getNumber()+1,
                orders.getTotalElements()
        );
    }
}
