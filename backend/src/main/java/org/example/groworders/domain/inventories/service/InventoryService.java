package org.example.groworders.domain.inventories.service;

import lombok.*;
import org.example.groworders.domain.farms.model.dto.FarmDto;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.farms.repository.FarmRepository;
import org.example.groworders.domain.inventories.model.dto.InventoryDto;
import org.example.groworders.domain.inventories.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final FarmRepository farmRepository;

    //재고 등록
    public void register(InventoryDto.Register dto) {
        inventoryRepository.save(dto.toEntity());
    }

    //재고 상세 조회
    public void details() {

    }


    //재고 목록 조회
    public FarmDto.FarmRes list(Long farmId) {
        Farm farm = farmRepository.findById(farmId).get();
        return FarmDto.FarmRes.from(farm);
    }
}
