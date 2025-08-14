package org.example.groworders.domain.inventories.model.dto;

import lombok.*;
import org.example.groworders.domain.inventories.model.entity.Inventory;

import java.util.List;

@Getter
@Builder
public class InventoryList {
    private List<InventoryDto.InventoryRes> inventories;

    public static InventoryList from(List<Inventory> entities) {
        return InventoryList.builder()
                .inventories(entities.stream().map(InventoryDto.InventoryRes::from).toList())
                .build();
    }
}