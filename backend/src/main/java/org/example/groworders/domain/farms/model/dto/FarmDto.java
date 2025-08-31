package org.example.groworders.domain.farms.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.groworders.domain.crops.model.dto.CropDto;
import org.example.groworders.domain.crops.model.entity.Crop;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.users.model.entity.User;

import java.util.List;

public class FarmDto {

    // 농장 등록
    @Getter
    public static class Register {
        @NotNull
        private Long userId;

        @NotBlank(message = "농장 이름를 입력해주세요.")
        @Size(max = 20)
        private String name;

        @NotBlank(message = "농장 지역은 필수 선택입니다.")
        private String region;

        @NotBlank(message = "농장 주소를 입력해주세요.")
        @Size(max = 50)
        private String address;

        @NotNull(message = "농장 면적을 입력해주세요.")
        @Min(value = 10)
        private Integer size;

        @NotBlank(message = "농장 설명을 입력해주세요.")
        @Size(max = 100)
        private String contents;

        private String profile_image_url; // 농장 프로필 사진은 필수 아님

        public Farm toEntity(Long userId) {
            User user = User.builder()
                    .id(userId)
                    .build();

            return Farm.builder()
                    .name(name)
                    .region(region)
                    .address(address)
                    .size(size)
                    .contents(contents)
                    .profile_image_url(profile_image_url)
                    .user(user)
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
        private List<CropDto.CropResponse> cropList;

        public static FarmResponse from(Farm entity, String presignedUrl) {
            return FarmResponse.builder()
                    .user_id(entity.getUser().getId())
                    .id(entity.getId())
                    .name(entity.getName())
                    .region(entity.getRegion())
                    .address(entity.getAddress())
                    .size(entity.getSize())
                    .contents(entity.getContents())
                    .profile_image_url(presignedUrl)
                    .cropList(entity.getCropList().stream().map(CropDto.CropResponse::from).toList())
                    .build();
        }
    }

    //로그인 시 농장 정보 응답할 데이터
    @Getter
    @Builder
    public static class OwnedFarm {
        private Long id;
        private String name;
        private List<String> cropType;

        public static OwnedFarm from(Farm entity) {
            return OwnedFarm.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .cropType(entity.getCropList().stream().map(Crop::getType).distinct().toList()) //Crop Entity에서 Type 중복 제거하고 가져오기
                    .build();
        }
    }

}