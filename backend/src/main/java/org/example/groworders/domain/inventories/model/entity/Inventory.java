package org.example.groworders.domain.inventories.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.groworders.domain.crops.model.Crop;

import java.util.Date;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date expectedHarvestDate; //예측 수확일

    private Integer expectedQuantity; //예측 수확량

    private Integer maxExpectedQuantity; //최대 수확량

    @OneToOne
    @JoinColumn(name = "crop_id")
    private Crop crop; //작물 어떤 작물인지 : 외래키
}
