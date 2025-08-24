package org.example.groworders.domain.inventories.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.groworders.domain.crops.model.entity.Crop;

import java.time.LocalDate;

public class InventoryDto {
    //재고 등록 및 수정 요청 데이터
    @Getter
    @Builder
    public static class Register {
        @NotNull(message = "예측 수확일을 확인 해주세요.")
        @Future(message = "예측 수확일은 현재 이후만 가능합니다.")
        private LocalDate expectedHarvestDate;

        @NotNull(message="예측 수확량을 확인 해주세요.")
        @PositiveOrZero(message = "예측 수확량은 숫자만 가능합니다.") //0이상 숫자
        private Integer expectedQuantity;

        @NotNull(message = "최대 예측 수확량을 확인 해주세요.")
        @PositiveOrZero(message = "최대 예측 수확량은 숫자만 가능합니다.") //0이상 숫자
        private Integer maxExpectedQuantity;

        @NotNull(message = "작물은 필수 입력 사항입니다.")
        @Positive(message = "작물을 확인 해주세요.") //1이상 숫자
        private Long cropId;
    }
}
