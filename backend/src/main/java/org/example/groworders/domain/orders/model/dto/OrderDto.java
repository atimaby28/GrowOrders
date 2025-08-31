package org.example.groworders.domain.orders.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.example.groworders.domain.crops.model.entity.Crop;
import org.example.groworders.domain.farm.model.entity.Farm;
import org.example.groworders.domain.orders.model.entity.Order;
import org.example.groworders.domain.orders.model.entity.ShippingStatus;
import org.example.groworders.domain.users.model.dto.UserDto;
import org.example.groworders.domain.users.model.entity.User;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;


public class OrderDto {
    @Getter
    @Builder
    public static class OrderResFarmer {
        private Long id;
        private Long farmId;//farm 관계
        private Long userId; //user 관계
        private String name; //user 관계
        private String phoneNumber; //user 관계
        private String cropName; // crop 관계
        private Integer quantity;
        private Integer totalPrice;

        @Enumerated(EnumType.STRING)
        private ShippingStatus shippingStatus;
        private LocalDateTime orderDate;


        public static OrderResFarmer from(Order entity) {
            return OrderResFarmer.builder()
                    .id(entity.getId())
                    .farmId(entity.getFarmOrder().getId())
                    .userId(entity.getUserOrder().getId())
                    .name(entity.getUserOrder().getName())
                    .phoneNumber(entity.getUserOrder().getPhoneNumber())
                    .cropName(entity.getCrop())
                    .quantity(entity.getQuantity())
                    .totalPrice(entity.getTotalPrice())
                    .shippingStatus(entity.getShippingStatus())
                    .orderDate(entity.getCreatedAt())
                    .build();

        }

    }


    @Getter
    @Builder
    public static class OrderResBuyer {
        private Long id;
        private Long farmId;//farm 관계
        private String farmName; //farm 관계
        private Long userId; //user 관계
        private String farmOwnerPhone; //farm-user 관계
        private String cropName; // crop 관계
        private Integer quantity;
        private Integer totalPrice;

        @Enumerated(EnumType.STRING)
        private ShippingStatus shippingStatus;
        private LocalDateTime orderDate;


        public static OrderResBuyer from(Order entity) {
            return OrderResBuyer.builder()
                    .id(entity.getId())
                    .farmId(entity.getFarmOrder().getId())
                    .farmName(entity.getFarmOrder().getName())
                    .userId(entity.getUserOrder().getId())
                    .farmOwnerPhone(entity.getFarmOrder().getUser().getPhoneNumber())
                    .cropName(entity.getCrop())
                    .quantity(entity.getQuantity())
                    .totalPrice(entity.getTotalPrice())
                    .shippingStatus(entity.getShippingStatus())
                    .orderDate(entity.getCreatedAt())
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
        @NotNull
        private Long cropId;
        private String cropName;
        @Min(value = 1, message = "1 이상의 정수만 입력하세요")
        private Integer quantity;
        @NotNull
        private Integer price;
        private Integer totalPrice;
        @Pattern(message = "유효한 주소를 입력하세요", regexp = "^[가-힣0-9\\s\\-\\.]+$")
        private String address;
        @Pattern(regexp = "^[가-힣0-9\\s.,\\-!()]*$", message = "유효한 요청사항을 입력하세요")
        private String deliveryRequest; // 실제로는 "배달 요청사항"
        @Pattern(regexp = "^[가-힣0-9\\s.,\\-!()]*$", message = "유효한 요청사항을 입력하세요")
        private String orderRequest;    // 실제로는 "주문 요청사항"

        public Order toEntity(UserDto.AuthUser authUser) {
            return Order.builder()
                    .name(name)
                    .crop(cropName)//작물이름
                    .price(price)//작물가격
                    .quantity(quantity)
                    .totalPrice(price* quantity)
                    .address(address)
                    .deliveryRequest(deliveryRequest)
                    .orderRequest(orderRequest)
                    .shippingStatus(ShippingStatus.PREPARING)
                    .userOrder(User.builder().id(authUser.getId()).build())
                    .cropOrder(Crop.builder().id(cropId).build())
                    .build();

        }

        public static Register from(Order entity) {
            return Register.builder()
                    .name(entity.getName())
                    .cropId(entity.getCropOrder().getId())
                    .cropName(entity.getCropOrder().getType())
                    .price(entity.getPrice())
                    .quantity(entity.getQuantity())
                    .totalPrice(entity.getTotalPrice())
                    .address(entity.getAddress())
                    .deliveryRequest(entity.getDeliveryRequest())
                    .orderRequest(entity.getOrderRequest())
                    //.shippingStatus(entity.getShippingStatus())
                    .build();

        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Modify {
        private Long id;
        @Pattern(message = "이름은 한글만 가능합니다", regexp = "^[가-힣]{1,50}$")
        private String name;
        private String cropName;
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
        private String cancel;

        public static Modify from(Order entity) {
            return Modify.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .cropName(entity.getCrop())
                    .price(entity.getPrice())
                    .quantity(entity.getQuantity())
                    .totalPrice(entity.getTotalPrice())
                    .address(entity.getAddress())
                    .deliveryRequest(entity.getDeliveryRequest())
                    .orderRequest(entity.getOrderRequest())
                    .cancel(entity.getCancel())
                    .build();

        }

        public Order updateEntity(Order order, UserDto.AuthUser authUser) {

            return Order.builder()
                    .id(order.getId())
                    .name(name != null ? name : order.getName())
                    .crop(order.getCrop())
                    .price(order.getPrice())
                    .quantity(quantity != null ? quantity : order.getQuantity())
                    .totalPrice(order.getPrice() * (quantity != null ? quantity : order.getQuantity()))
                    .address(address != null ? address : order.getAddress())
                    .deliveryRequest(deliveryRequest != null ? deliveryRequest : order.getDeliveryRequest())
                    .orderRequest(orderRequest != null ? orderRequest : order.getOrderRequest())
                    .shippingStatus(ShippingStatus.PREPARING)
                    .cancel(cancel != null ? cancel : order.getCancel())
                    .userOrder(User.builder().id(authUser.getId()).build())// 관계 객체 유지
                    .farmOrder(order.getFarmOrder())
                    .cropOrder(order.getCropOrder())
                    .build();
        }
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderList<T> {
        private List<T> content;
        @Schema(description = "페이지 번호, 1번부터 시작", required = true, example = "1")
        private int pageNumber;
        @Schema(description = "각 페이지당 게시글 수", required = true, example = "10")
        private int pageSize;
        private long totalElements;
        private int totalPages;

        public static <T, E> OrderList<T> from(Page<E> page, Function<E, T> converter) {
            return OrderList.<T>builder()
                    .content(page.getContent().stream().map(converter).toList())
                    .pageNumber(page.getNumber() + 1) // 0 → 1 변환
                    .pageSize(page.getSize())
                    .totalElements(page.getTotalElements())
                    .totalPages(page.getTotalPages())
                    .build();
        }
    }


}


