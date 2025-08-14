package org.example.groworders.domain.inventories.model.dto;

import lombok.*;
import org.example.groworders.domain.crops.model.Crop;
import org.example.groworders.domain.inventories.model.entity.Inventory;

import java.util.Date;

public class InventoryDto {
    //재고 등록 요청 데이터
    @Getter
    public static class Register {
        private Date expectedHarvestDate; //예측 수확일
        private Integer expectedQuantity; //예측 수확량
        private Integer maxExpectedQuantity; //최대 예측 수확량
        private Long cropId; //등록할 작물

        public Inventory toEntity() {
            Crop crop = Crop.builder()
                    .id(cropId)
                    .build();

            return Inventory.builder()
                    .expectedHarvestDate(expectedHarvestDate)
                    .expectedQuantity(expectedQuantity)
                    .maxExpectedQuantity(maxExpectedQuantity)
                    .crop(crop)
                    .build();
        }
    }

    //응답 데이터
    @Getter
    @Builder
    public static class InventoryRes {
        private Long id;
        private Long cropId;
        private String cropType;
        private String cropState;
        private Integer orderQuantity;
        private Date expectedHarvestDate;
        private Integer expectedQuantity;
        private Integer maxExpectedQuantity;

        public static InventoryRes from(Inventory entity) {
            return InventoryRes.builder()
                    .id(entity.getId())
                    .cropId(entity.getCrop().getId())
                    .cropType(entity.getCrop().getType())
                    .cropState(entity.getCrop().getState())
                    .expectedHarvestDate(entity.getExpectedHarvestDate())
                    .expectedQuantity(entity.getExpectedQuantity())
                    .maxExpectedQuantity(entity.getMaxExpectedQuantity())
                    .build();
        }
    }
}
