//package org.example.groworders.domain.farm.model.dto;
//
//import lombok.*;
//import org.example.groworders.domain.orders.model.entity.Order;
//import org.example.groworders.domain.orders.model.entity.ShippingStatus;
//
//public class Farmdto {
//    @Getter
//    @Setter
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class Register {
//        private String name;
//        private String crop;
//        private Integer price;
//        private Integer quantity;
//        private Integer totalPrice;
//        private String address;
//        private String deliveryRequest; // 실제로는 "배달 요청사항"
//        private String orderRequest;    // 실제로는 "주문 요청사항"
//        private ShippingStatus shippingStatus;
//
//        public Order toEntity() {
//            return Order.builder()
//                    .name(name)
//                    .crop(crop)
//                    .price(price)
//                    .quantity(quantity)
//                    .totalPrice(totalPrice)
//                    .address(address)
//                    .deliveryRequest(deliveryRequest)
//                    .orderRequest(orderRequest)
//                    .shippingStatus(shippingStatus)
//                    .build();
//
//        }
//    }
//
//}
//}
