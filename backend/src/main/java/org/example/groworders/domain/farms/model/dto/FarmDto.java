package org.example.groworders.domain.farms.model.dto;

import lombok.*;
import org.example.groworders.domain.crops.model.Crop;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.inventories.model.dto.InventoryDto;
import org.example.groworders.domain.users.model.entity.User;

import java.util.List;
import java.util.Objects;

public class FarmDto {

    @Getter
    public class FarmReq {
        private String name;
        private String description;
        private Long userId;

        public Farm toEntity() {
            User user = User.builder()
                    .id(userId)
                    .build();

            return Farm.builder()
                    .name(name)
                    .description(description)
                    .user(user)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class FarmRes {
        private Long id;
        private String name;
        private String description;
        private String owner;
        private List<InventoryDto.InventoryRes> inventories;

        public static FarmRes from(Farm entity) {
            return FarmRes.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .description(entity.getDescription())
                    .owner(entity.getUser().getName())
                    .inventories(entity.getCrops().stream().map(Crop::getInventory)
                            .filter(Objects::nonNull) // null 방지
                            .map(InventoryDto.InventoryRes::from)
                            .toList())
                    .build();
        }
    }
}
