package com.example.greenproject.model.enums;

public enum OrderStatus {
    INIT,
    PENDING,//chờ xác nhận
    PROCESSING,//đang xử lý
    SHIPPED,//đang giao hàng
    DELIVERED,//đã giao
    CANCELED,//hủy
    RETURNED//đã trả hàng
}