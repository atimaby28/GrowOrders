package org.example.groworders.domain.crops.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.inventories.model.dto.InventoryDto;

import java.util.Date;

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
    private Date sowingStartDate; //파종 시작일
    private Integer area;  //재배 면적

    @Temporal(TemporalType.DATE)
    private Date expectedHarvestDate; //예측 수확일
    private Integer expectedQuantity; //예측 수확량
    private Integer maxExpectedQuantity; //최대 수확량

    @ManyToOne
    @JoinColumn(name="farm_id")
    private Farm farm; //작물을 소유한 농장 : 외래키

    public void updateInventory(InventoryDto.Register dto) {
        expectedHarvestDate = dto.getExpectedHarvestDate();
        expectedQuantity = dto.getExpectedQuantity();
        maxExpectedQuantity = dto.getMaxExpectedQuantity();
    }
}
