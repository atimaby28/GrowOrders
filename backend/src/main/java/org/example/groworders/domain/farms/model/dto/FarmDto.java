package org.example.groworders.domain.farms.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.groworders.domain.crops.model.entity.Crop;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.users.model.entity.User;

import java.util.List;

public class FarmDto {

    // 농장 등록
    @Getter
    @Setter
    public static class Register {
        @Size(max = 20) @NotBlank
        private String name;

        @NotBlank
        private String region;

        @Size(max = 50) @NotBlank
        private String address;

        @Min(value = 10)
        private Integer size;

        @Size(max = 100)
        private String contents;

        private String profile_image_url;

        public Farm toEntity(User user) {
            Farm entity = Farm.builder()
                    .name(name)
                    .region(region)
                    .address(address)
                    .size(size)
                    .contents(contents)
                    .profile_image_url(profile_image_url)
                    .user(user)
                    .build();
            return entity;
        }

//        public Farm toEntity(FarmDto.Register dto) {
//            Farm entity = Farm.builder()
//                    .name(name)
//                    .region(region)
//                    .address(address)
//                    .size(size)
//                    .contents(contents)
//                    .profile_image_url(profile_image_url)
//                    .build();
//            return entity;
//        }

    }

    // 농장 리스트
    @Builder
    @Getter
    public static class FarmList {
        private Boolean isSuccess;
        private List<FarmResponse> result;

        public static FarmList from(List<Farm> entityList) {
            return FarmList.builder()
                    .isSuccess(true)
                    .result(entityList.stream().map(entity -> FarmResponse.from(entity)).toList())
                    .build();
        }
    }

    // FarmResponse
    @Getter
    @Builder
    public static class FarmResponse {
        private Long user_id;
        private Long id;
        private String name;
        private String region;
        private String address;
        private Integer size;
        private String contents;
        private String profile_image_url;
        private List<Crop> cropList;

        public static FarmResponse from(Farm entity) {
            return FarmResponse.builder()
                    .user_id(entity.getUser().getId())
                    .id(entity.getId())
                    .name(entity.getName())
                    .region(entity.getRegion())
                    .address(entity.getAddress())
                    .size(entity.getSize())
                    .contents(entity.getContents())
                    .profile_image_url(entity.getProfile_image_url())
//                    .cropList(entity.getCropList())
                    .build();
        }
    }


}