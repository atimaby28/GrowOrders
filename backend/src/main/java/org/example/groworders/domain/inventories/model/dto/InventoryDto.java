package org.example.groworders.domain.inventories.model.dto;

import lombok.*;
import org.example.groworders.domain.crops.model.entity.Crop;

import java.util.Date;

public class InventoryDto {
    //재고 등록 및 수정 요청 데이터
    @Getter
    public static class Register {
        private Date expectedHarvestDate; //예측 수확일
        private Integer expectedQuantity; //예측 수확량
        private Integer maxExpectedQuantity; //최대 예측 수확량
        private Long cropId; //등록할 작물

        public Crop toEntity() {
            return Crop.builder()
                    .expectedHarvestDate(expectedHarvestDate)
                    .expectedQuantity(expectedQuantity)
                    .maxExpectedQuantity(maxExpectedQuantity)
                    .build();
        }
    }
}
