package org.example.groworders.domain.farms.model.dto;

import lombok.*;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.inventories.model.dto.InventoryDto;
import org.example.groworders.domain.users.model.entity.User;

import java.util.List;
import java.util.Objects;

public class FarmDto {

    @Getter
    public static class Register {
        private String name;
        private String region;
        private String address;
        private Integer size;
        private String contents;
        private String profile_image_url;
        private Long userId;

        public Farm toEntity() {
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

    @Getter
    @Builder
    public static class FarmResponse {
        private Long id;
        private String name;
        private String region;
        private String address;
        private Integer size;
        private String contents;
        private String profile_image_url;
        private String owner;

        public static FarmResponse from(Farm entity) {
            return FarmResponse.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .region(entity.getRegion())
                    .address(entity.getAddress())
                    .size(entity.getSize())
                    .contents(entity.getContents())
                    .profile_image_url(entity.getProfile_image_url())
                    .build();
        }
    }
}
