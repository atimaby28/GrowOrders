package org.example.groworders.domain.orders.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;
    private String name;
    private String crop;
    private Integer price;
    private Integer quantity;
    private Integer totalPrice;
    private String address;
    private String deliveryRequest;
    private String orderRequest;

    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus;

    //private String cancel;
    //private Long userIdx;
    //private Integer cropIdx;


//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

//    @ManyToOne
//    @JoinColumn(name = "farm_id")
//    private Farm farm;

//    @ManyToOne
//    @JoinColumn(name = "crop_id")
//    private Crop crop;

}

