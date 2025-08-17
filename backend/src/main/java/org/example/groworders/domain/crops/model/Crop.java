package org.example.groworders.domain.crops.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.inventories.model.entity.Inventory;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type; //작물 종류(이름)
    private String state; //작물 상태

    @ManyToOne
    @JoinColumn(name="farm_id")
    private Farm farm; //작물을 소유한 농장 : 외래키

    @OneToOne(mappedBy = "crop")
    private Inventory inventory;
}
