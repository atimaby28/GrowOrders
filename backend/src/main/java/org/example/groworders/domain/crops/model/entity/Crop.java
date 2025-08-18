package org.example.groworders.domain.crops.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.inventories.model.dto.InventoryDto;
import org.hibernate.annotations.*;

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

    //작물 종류(이름) : not null, 길이 50
    @Column(nullable = false, length = 50)
    private String type;

    //작물 상태 : 길이 2, 기본값 '양호'
    @Column(length = 2)
    @ColumnDefault("'양호'")
    private String state;

    //파종 시작일 : YYYY-MM-DD, 기본값 현재 시간
    @Temporal(TemporalType.DATE)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Date sowingStartDate;

    //재배 면적 : 기본값 0, 0 혹은 양수
    @PositiveOrZero
    @ColumnDefault("0")
    private Integer area;

    @Temporal(TemporalType.DATE)
    private Date expectedHarvestDate; //예측 수확일
    private Integer expectedQuantity; //예측 수확량
    private Integer maxExpectedQuantity; //최대 수확량

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm; //작물을 소유한 농장 : 외래키

    public void updateInventory(InventoryDto.Register dto) {
        expectedHarvestDate = dto.getExpectedHarvestDate();
        expectedQuantity = dto.getExpectedQuantity();
        maxExpectedQuantity = dto.getMaxExpectedQuantity();
    }
}
