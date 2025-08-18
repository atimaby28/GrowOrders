package org.example.groworders.domain.crops.model.dto;

import lombok.*;
import org.example.groworders.domain.crops.model.entity.Crop;
import org.example.groworders.domain.farms.model.entity.Farm;

import java.util.Date;

public class CropDto {

    //작물 등록 요청 데이터
    @Getter
    public static class Register {
        private String type; //작물 종류(이름)
        private String state; //작물 상태
        private Date sowingStartDate; //파종 시작일
        private Integer area;  //재배 면적
        private Long farmId; //등록할 농장 아이디

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
