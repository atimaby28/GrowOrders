package org.example.groworders.domain.orders.model.dto;

import lombok.*;
import org.example.groworders.domain.orders.model.entity.Order;
import org.example.groworders.domain.orders.model.entity.ShippingStatus;

public class OrderDto {
    @Getter
    @Builder
    public static class OrderRes{
        private Integer idx;
        // private Integer farmId;
        private String farmName;
        // private Integer cropId;
        private String cropName;
        private String cultivationMethod;
        private Integer cultivationArea;
        private Integer price;

        public static OrderRes from(Order entity){
            return OrderRes.builder()
                    .idx(entity.getIdx())
                    //.farmIdx(entity.getFarmId())
                    //.farmName(entity.getFarm().getFarmName())
                    //.cropId(entity.getCropId())
                    //.cropName(entity.getInventory().getCropName())
                    //.cultivationMethod(entity.getCultivationMethod())
                    //.cultivationArea(entity.getCultivationArea())
                    .price(entity.getPrice())
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
    }


//    @Getter
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class Register {
//        private String name;    //주문자성명
//        private String crop;    //품목명
//        private Integer price;  //단가
//        private Integer quantity;   //수량
//        private Integer totalPrice;  //총합
//        private String address; //주소
//        private String deliveryRequest; //주문요구사항
//        private String orderRequest;    //배달요구사항
//        private ShippingStatus shippingStatus;
//
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
////                    .shippingStatus(shippingStatus)
//                    //.user_idx()
//                    //.crop_idx()
//                    .build();
//
//        }
//
//
//    }
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
}
