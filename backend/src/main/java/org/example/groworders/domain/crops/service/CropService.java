package org.example.groworders.domain.crops.service;

import lombok.RequiredArgsConstructor;
import org.example.groworders.domain.crops.model.dto.CropDto;
import org.example.groworders.domain.crops.repository.CropRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CropService {
    private final CropRepository cropRepository;

    //작물 등록
    public void register(CropDto.Register dto) {
        cropRepository.save(dto.toEntity());
    }
}
