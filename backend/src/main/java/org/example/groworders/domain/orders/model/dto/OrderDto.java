package org.example.groworders.domain.orders.model.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.example.groworders.domain.orders.model.entity.Order;
import org.example.groworders.domain.orders.model.entity.ShippingStatus;



public class OrderDto {
    @Getter
    @Builder
    public static class OrderResFarmer {
        private Integer id;
        private Long userId; //user 관계
        private String name; //user 관계
        private String phoneNumber; //user 관계
        private Long cropId; //crop 관계
        private String cropName; // crop 관계
        private Integer quantity;
        private Integer totalPrice;

        @Enumerated(EnumType.STRING)
        private ShippingStatus shippingStatus;


        public static OrderResFarmer from(Order entity) {
            return OrderResFarmer.builder()
                    .id(entity.getId())
                    .userId(entity.getUserOrder().getId())
                    .name(entity.getUserOrder().getName())
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


        public static OrderResBuyer from(Order entity) {
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
        @Pattern(message = "이름은 한글만 가능합니다", regexp = "^[가-힣]{1,50}$")
        private String name;
        private String crop;
        private Integer price;
        @Min(value = 1, message = "1 이상의 정수만 입력하세요")
        private Integer quantity;
        private Integer totalPrice;

        @Pattern(message = "유효한 주소를 입력하세요", regexp = "^[가-힣0-9\\s\\-\\.]+$")
        private String address;
        @Pattern(regexp = "^[가-힣0-9\\s.,\\-!()]*$", message = "유효한 요청사항을 입력하세요")
        private String deliveryRequest; // 실제로는 "배달 요청사항"
        @Pattern(regexp = "^[가-힣0-9\\s.,\\-!()]*$", message = "유효한 요청사항을 입력하세요")
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

        public static Register from(Order entity) {
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
        @Pattern(message = "이름은 한글만 가능합니다", regexp = "^[가-힣]{1,50}$")
        private String name;
        private String crop;
        private Integer price;
        @Min(value = 0, message = "0 이상의 정수만 입력하세요")
        private Integer quantity;
        private Integer totalPrice;
        @Pattern(message = "유효한 주소를 입력하세요", regexp = "^[가-힣0-9\\s\\-\\.]+$")
        private String address;
        @Pattern(regexp = "^[가-힣0-9\\s.,\\-!()]*$", message = "유효한 요청사항을 입력하세요")
        private String deliveryRequest; // 실제로는 "배달 요청사항"
        @Pattern(regexp = "^[가-힣0-9\\s.,\\-!()]*$", message = "유효한 요청사항을 입력하세요")
        private String orderRequest;    // 실제로는 "주문 요청사항"
        private ShippingStatus shippingStatus;
        private String cancel;

        public static Modify from(Order entity) {
            return Modify.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .crop(entity.getCrop())
                    .price(entity.getPrice())
                    .quantity(entity.getQuantity())
                    .totalPrice(entity.getTotalPrice())
                    .address(entity.getAddress())
                    .deliveryRequest(entity.getDeliveryRequest())
                    .orderRequest(entity.getOrderRequest())
                    .shippingStatus(entity.getShippingStatus())
                    .cancel(entity.getCancel())
                    .build();

        }

        public Order updateEntity(Order order) {
//            if (name != null) order.setName(name);
//            if (crop != null) order.setCrop(crop);
//            if (price != null) order.setPrice(price);
//            if (quantity != null) order.setQuantity(quantity);
//            if (totalPrice != null) order.setTotalPrice(totalPrice);
//            if (address != null) order.setAddress(address);
//            if (deliveryRequest != null) order.setDeliveryRequest(deliveryRequest);
//            if (orderRequest != null) order.setOrderRequest(orderRequest);
//            if (shippingStatus != null) order.setShippingStatus(shippingStatus);
//            if (cancel != null) order.setCancel(cancel);

            return Order.builder()
                    .id(order.getId())
                    .name(name != null ? name : order.getName())
                    .crop(crop != null ? crop : order.getCrop())
                    .price(price != null ? price : order.getPrice())
                    .quantity(quantity != null ? quantity : order.getQuantity())
                    .totalPrice(totalPrice != null ? totalPrice : order.getTotalPrice())
                    .address(address != null ? address : order.getAddress())
                    .deliveryRequest(deliveryRequest != null ? deliveryRequest : order.getDeliveryRequest())
                    .orderRequest(orderRequest != null ? orderRequest : order.getOrderRequest())
                    .shippingStatus(shippingStatus != null ? shippingStatus : order.getShippingStatus())
                    .cancel(cancel != null ? cancel : order.getCancel())
                    .orderDate(order.getOrderDate()) // 기존 주문 날짜 유지
                    .userOrder(order.getUserOrder()) // 관계 객체 유지
                    .farmOrder(order.getFarmOrder())
                    .cropOrder(order.getCropOrder())
                    .build();
        }
    }

}

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

