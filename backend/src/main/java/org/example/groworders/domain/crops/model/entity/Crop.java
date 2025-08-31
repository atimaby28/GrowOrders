package org.example.groworders.domain.crops.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.groworders.domain.farm.model.entity.Farm;
import org.example.groworders.domain.orders.model.entity.Order;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.List;

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

    private Integer price;

    //작물 상태 : 길이 2, 기본값 '양호'
    @Column(length = 2)
    @ColumnDefault("'양호'")
    private String state;

    //파종 시작일 : YYYY-MM-DD, 기본값 현재 시간
    @Temporal(TemporalType.DATE)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDate sowingStartDate;

    //재배 면적 : 기본값 0, 0 혹은 양수
    @ColumnDefault("0")
    private Integer area;

    //재배 방식 : not null, 길이 2
    @Column(nullable = false, length = 2)
    private String cultivateType;

    @Temporal(TemporalType.DATE)
    private LocalDate expectedHarvestDate; //예측 수확일
    private Integer expectedQuantity; //예측 수확량
    private Integer maxExpectedQuantity; //최대 수확량

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm; //작물을 소유한 농장 : 외래키

    @OneToMany(mappedBy = "cropOrder")
    private List<Order> orderList;

//    public void updateInventory(InventoryDto.Register dto) {
//        expectedHarvestDate = dto.getExpectedHarvestDate();
//        expectedQuantity = dto.getExpectedQuantity();
//        maxExpectedQuantity = dto.getMaxExpectedQuantity();
//    }
}