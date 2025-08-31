package org.example.groworders.domain.orders.model.entity;

public enum ShippingStatus {
    PREPARING, //상품준비중
    SHIPPED, // 배송중
    DELIVERED, //배송완료
    CANCELED, //환불
    EXCHANGED//교환
}
