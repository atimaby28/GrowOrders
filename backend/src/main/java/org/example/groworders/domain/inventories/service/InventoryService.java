package org.example.groworders.domain.inventories.service;

import lombok.*;
import org.example.groworders.common.exception.BaseException;
import org.example.groworders.domain.crops.model.dto.CropDto;
import org.example.groworders.domain.crops.model.entity.Crop;
import org.example.groworders.domain.crops.repository.CropRepository;
import org.example.groworders.domain.farms.model.dto.FarmDto;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.farms.repository.FarmRepository;
import org.example.groworders.domain.inventories.model.dto.InventoryDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.example.groworders.common.model.BaseResponseStatus.INVALID_CROP_INFO;
import static org.example.groworders.common.model.BaseResponseStatus.INVALID_FARM_INFO;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final CropRepository cropRepository;
    private final FarmRepository farmRepository;

    //재고 등록 및 수정
    public void save(InventoryDto.Register dto) {
        Crop crop = cropRepository.findById(dto.getCropId()).orElseThrow(() -> BaseException.from(INVALID_CROP_INFO));

        //작물이 존재하면 등록 및 수정
        crop.updateInventory(dto);
        cropRepository.save(crop);
    }

    //재고 상세 조회
    public CropDto.CropResponse details(Long cropId) {
        Crop crop = cropRepository.findById(cropId).orElseThrow(() -> BaseException.from(INVALID_CROP_INFO));
        return CropDto.CropResponse.from(crop);

    }


    //재고 목록 조회
    public FarmDto.FarmResponse list(Long farmId) {
        Farm farm = cropRepository.findByIdWithCrop(farmId).orElseThrow(() -> BaseException.from(INVALID_FARM_INFO));
        return FarmDto.FarmResponse.from(farm);
    }
}
