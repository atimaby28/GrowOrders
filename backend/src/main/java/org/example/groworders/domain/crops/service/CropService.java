package org.example.groworders.domain.crops.service;

import lombok.RequiredArgsConstructor;
import org.example.groworders.common.exception.BaseException;
import org.example.groworders.domain.crops.model.dto.CropDto;
import org.example.groworders.domain.crops.repository.CropRepository;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.farms.repository.FarmRepository;
import org.springframework.stereotype.Service;
import static org.example.groworders.common.model.BaseResponseStatus.INVALID_FARM_INFO;

@Service
@RequiredArgsConstructor
public class CropService {
    private final CropRepository cropRepository;
    private final FarmRepository farmRepository;

    //작물 등록
    public void register(CropDto.Register dto) {
        Farm farm = farmRepository.findById(dto.getFarmId()).orElseThrow(()-> BaseException.from(INVALID_FARM_INFO));

        //농장이 존재하면 작물 등록
        cropRepository.save(dto.toEntity());
    }
}
