package org.example.groworders.domain.crops.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.groworders.domain.crops.model.entity.Crop;
import org.example.groworders.domain.farms.model.entity.Farm;

import java.util.Date;

public class CropDto {

    //작물 등록 요청 데이터
    @Getter
    public static class Register {
        @Pattern(message = "작물 종류를 선택 해주세요.", regexp = "^(?:토마토|딸기|파프리카)$")
        private String type;

        @Pattern(message="작물 상태를 옳바르게 선택 해주세요.", regexp="^(?:양호|보통|불량)$")
        private String state;

        @Pattern(message = "파종 시작일이 날짜 형식에 어긋납니다.", regexp = "^(?:(?:19|20)\\d{2})-(?:(?:0[13578]|1[02])-(?:0[1-9]|[12]\\d|3[01])|(?:0[469]|11)-(?:0[1-9]|[12]\\d|30)|02-(?:0[1-9]|1\\d|2[0-8]))$|^(?:(?:19|20)(?:[02468][048]|[13579][26])|2000)-02-29$") //날짜 형식, 윤년까지 체크
        private Date sowingStartDate;

        @Pattern(message = "재배 면적은 숫자만 가능합니다.", regexp = "^\\d+$") //0이상 숫자
        private Integer area;

        @Pattern(message = "등록된 농장이 아닙니다.", regexp = "^[1-9]\\d*$") //1이상 숫자
        private Long farmId;

        public Crop toEntity() {
            Farm farm = Farm.builder()
                    .id(farmId)
                    .build();

            return Crop.builder()
                    .type(type)
                    .state(state)
                    .sowingStartDate(sowingStartDate)
                    .area(area)
                    .farm(farm)
                    .build();
        }
    }

    //응답 데이터
    @Getter
    @Builder
    public static class CropResponse {
        private Long id;
        private String type; //작물 종류(이름)
        private String state; //작물 상태
        private Date sowingStartDate; //파종 시작일
        private Integer area; //재배 면적
        private Integer orderQuantity; //주문 요청량
        private Date expectedHarvestDate; //예측 수확일
        private Integer expectedQuantity; //예측 수확량
        private Integer maxExpectedQuantity; //최대 수확량

        public static CropResponse from(Crop entity) {
            return CropResponse.builder()
                    .id(entity.getId())
                    .type(entity.getType())
                    .state(entity.getState())
                    .sowingStartDate(entity.getSowingStartDate())
                    .area(entity.getArea())
                    .expectedHarvestDate(entity.getExpectedHarvestDate())
                    .expectedQuantity(entity.getExpectedQuantity())
                    .maxExpectedQuantity(entity.getMaxExpectedQuantity())
                    .build();
        }
    }
}
