package org.example.groworders.domain.inventories.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.groworders.domain.crops.model.entity.Crop;

import java.util.Date;

public class InventoryDto {
    //재고 등록 및 수정 요청 데이터
    @Getter
    public static class Register {
        @Pattern(message = "예측 수확일이 날짜 형식에 어긋납니다.", regexp = "^(?:(?:19|20)\\d{2})-(?:(?:0[13578]|1[02])-(?:0[1-9]|[12]\\d|3[01])|(?:0[469]|11)-(?:0[1-9]|[12]\\d|30)|02-(?:0[1-9]|1\\d|2[0-8]))$|^(?:(?:19|20)(?:[02468][048]|[13579][26])|2000)-02-29$") //날짜 형식, 윤년까지 체크
        private Date expectedHarvestDate;

        @Pattern(message = "예측 수확량은 숫자만 가능합니다.", regexp = "^\\d+$") //0이상 숫자
        private Integer expectedQuantity;

        @Pattern(message = "최대 예측 수확량은 숫자만 가능합니다.", regexp = "^\\d+$") //0이상 숫자
        private Integer maxExpectedQuantity;

        @Pattern(message = "등록된 작물이 아닙니다.", regexp = "^[1-9]\\d*$") //1이상 숫자
        private Long cropId;

        public Crop toEntity() {
            return Crop.builder()
                    .expectedHarvestDate(expectedHarvestDate)
                    .expectedQuantity(expectedQuantity)
                    .maxExpectedQuantity(maxExpectedQuantity)
                    .build();
        }
    }
}
