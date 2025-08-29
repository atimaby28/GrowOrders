package org.example.groworders.domain.farms.model.dto;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.groworders.domain.crops.model.dto.CropDto;
import org.example.groworders.domain.crops.model.entity.Crop;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.users.model.entity.User;

import java.util.List;

public class FarmDto {

    // 농장 등록
    @Getter
    public static class Register {
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
    // 농장 정보
    @Getter
    public static class Read{
        private String name;
        private String region;
        private String address;
        private Integer size;
        private String contents;
        @Nullable
        private String profile_image_url;

        public static Farm from(Farm entity) {
            return Farm.builder()
                    .name(entity.getName())
                    .region(entity.getRegion())
                    .address(entity.getAddress())
                    .size(entity.getSize())
                    .contents(entity.getContents())
                    .profile_image_url(entity.getProfile_image_url())
                    .cropList(entity.getCropList())
                    .build();
        }
    }

    // 농장 정보 수정
    @Getter
    public static class Update {
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

        private String profile_image_url;

        public void update(Farm entity) {
            entity.setName(name);
            entity.setRegion(region);
            entity.setAddress(address);
            entity.setSize(size);
            entity.setContents(contents);
            entity.setProfile_image_url(profile_image_url);
        }
    }

    // 농장 리스트
    @Builder
    @Getter
    public static class FarmList {
        private Boolean isSuccess;
        private List<FarmResponse> farmResult;
        public static FarmList from(List<Farm> entityList) {
            return FarmList.builder()
                    .isSuccess(true)
                    .farmResult(entityList.stream().map(entity -> FarmResponse.from(entity)).toList())
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
                    //.cropList(entity.getCropList().stream().map(CropDto.CropResponse::from).toList())
                    .build();
        }
    }


}