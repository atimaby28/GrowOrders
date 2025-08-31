package org.example.groworders.domain.orders.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.groworders.common.model.BaseEntity;
import org.example.groworders.domain.crops.model.entity.Crop;
import org.example.groworders.domain.farm.model.entity.Farm;
import org.example.groworders.domain.users.model.entity.User;


@Getter
@Entity
@Table(name = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 10)
    private String crop;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(length = 100)
    private String address;

    @Column(length = 200)
    private String deliveryRequest;

    @Column(length = 200)
    private String orderRequest;

    @Column(length = 10)
    private String cancel;


    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus;


    @ManyToOne//(fetch = FetchType.Lazy)
    @JoinColumn(name = "user_idx")
    private User userOrder;

    @ManyToOne//(fetch = FetchType.Lazy)
    @JoinColumn(name = "farm_id")
    private Farm farmOrder;

    @ManyToOne//(fetch = FetchType.Lazy)
    @JoinColumn(name = "crop_id")
    private Crop cropOrder;

}

