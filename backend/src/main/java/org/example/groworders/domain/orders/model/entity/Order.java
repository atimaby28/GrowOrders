package org.example.groworders.domain.orders.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.groworders.domain.crops.model.entity.Crop;
import org.example.groworders.domain.farm.model.entity.Farm;
import org.example.groworders.domain.users.model.entity.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String crop;
    private Integer price;
    private Integer quantity;
    private Integer totalPrice;
    private String address;
    private String deliveryRequest;
    private String orderRequest;
    private String cancel;

    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus;

    @CreationTimestamp
    private LocalDateTime orderDate;

    //private String cancel;
    //private Long userIdx;
    //private Integer cropIdx;


    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User userOrder;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farmOrder;

    @ManyToOne
    @JoinColumn(name = "crop_id")
    private Crop cropOrder;

}

