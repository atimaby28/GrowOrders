package org.example.groworders.domain.orders.model.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.example.groworders.domain.orders.model.entity.Order;
import org.example.groworders.domain.orders.model.entity.ShippingStatus;



public class OrderDto {
    @Getter
    @Builder
    public static class OrderResBuyer {
        private Integer id;
        private Long farmId; //farm 관계
        private String farmName; //farm 관계
        private String phoneNumber; //user 관계
        private Long cropId; //crop 관계
        private String cropName; // crop 관계
        private Integer quantity;
        private Integer totalPrice;

        @Enumerated(EnumType.STRING)
        private ShippingStatus shippingStatus;


        public static OrderResBuyer from(Order entity){
            return OrderResBuyer.builder()
                    .id(entity.getId())
                    .farmId(entity.getFarmOrder().getId())
                    .farmName(entity.getFarmOrder().getName())
                    .phoneNumber(entity.getUserOrder().getPhoneNumber())
                    .cropId(entity.getCropOrder().getId())
                    .cropName(entity.getCropOrder().getCropName())
                    .quantity(entity.getQuantity())
                    .totalPrice(entity.getTotalPrice())
                    .shippingStatus(entity.getShippingStatus())
                    .build();

        }

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Register {
        private String name;
        private String crop;
        private Integer price;
        private Integer quantity;
        private Integer totalPrice;
        private String address;
        private String deliveryRequest; // 실제로는 "배달 요청사항"
        private String orderRequest;    // 실제로는 "주문 요청사항"
        private ShippingStatus shippingStatus;

        public Order toEntity() {
            return Order.builder()
                    .name(name)
                    .crop(crop)
                    .price(price)
                    .quantity(quantity)
                    .totalPrice(totalPrice)
                    .address(address)
                    .deliveryRequest(deliveryRequest)
                    .orderRequest(orderRequest)
                    .shippingStatus(shippingStatus)
                    .build();

        }

        public static Register from(Order entity){
            return Register.builder()
                    .name(entity.getName())
                    .crop(entity.getCrop())
                    .price(entity.getPrice())
                    .quantity(entity.getQuantity())
                    .totalPrice(entity.getTotalPrice())
                    .address(entity.getAddress())
                    .deliveryRequest(entity.getDeliveryRequest())
                    .orderRequest(entity.getOrderRequest())
                    .shippingStatus(entity.getShippingStatus())
                    .build();

        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Modify {
        private Integer id;
        private String name;
        private String crop;
        private Integer price;
        private Integer quantity;
        private Integer totalPrice;
        private String address;
        private String deliveryRequest; // 실제로는 "배달 요청사항"
        private String orderRequest;    // 실제로는 "주문 요청사항"
        private ShippingStatus shippingStatus;
        private String cancel;

//        public Modify updateEntity(OrderDto.Modify dto) {
//
//        }

//        public Order toEntity() {
//            return Order.builder()
//                    .id(id)
//                    .name(name)
//                    .crop(crop)
//                    .price(price)
//                    .quantity(quantity)
//                    .totalPrice(totalPrice)
//                    .address(address)
//                    .deliveryRequest(deliveryRequest)
//                    .orderRequest(orderRequest)
//                    .shippingStatus(shippingStatus)
//                    .cancel(cancel)
//                    .build();
//
//        }

//        public static Register from(Order entity){
//            return Register.builder()
//                    .name(entity.getName())
//                    .crop(entity.getCrop())
//                    .price(entity.getPrice())
//                    .quantity(entity.getQuantity())
//                    .totalPrice(entity.getTotalPrice())
//                    .address(entity.getAddress())
//                    .deliveryRequest(entity.getDeliveryRequest())
//                    .orderRequest(entity.getOrderRequest())
//                    .shippingStatus(entity.getShippingStatus())
//                    .cancel(entity.getCancel())
//                    .build();
//
//        }
    }

}

//      @Getter
//      @Builder
//    public static class Modify {
//        private Integer id;
//        private String name;    //주문자성명
//        private String crop;    //품목명
//        private Integer price;  //단가
//        private Integer quantity;   //수량
//        private Integer total;  //총합
//        private String address; //주소
//        private String deliveryRequest; //주문요구사항
//        private String orderRequest;    //배달요구사항
//        private String cancel; //환불OR교환
//
//
//        public Order toEntity() {
//            Order entity = Order.builder()
//                    .id(id)
//                    .name(name)
//                    .crop(crop)
//                    .price(price)
//                    .quantity(quantity)
//                    .total(total)
//                    .address(address)
//                    .deliveryRequest(deliveryRequest)
//                    .orderRequest(orderRequest)
//                    .cancel(cancel)
//                    //.user_id()
//                    //.crop_id()
//                    .build();
//            return entity;
//
//        }
//
//    }

