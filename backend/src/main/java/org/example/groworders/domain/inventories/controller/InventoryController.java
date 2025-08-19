package org.example.groworders.domain.inventories.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.*;
import org.example.groworders.common.model.BaseResponse;
import org.example.groworders.domain.crops.model.dto.CropDto;
import org.example.groworders.domain.farms.model.dto.FarmDto;
import org.springframework.web.bind.annotation.*;
import org.example.groworders.domain.inventories.model.dto.InventoryDto;
import org.example.groworders.domain.inventories.service.InventoryService;
import org.springframework.http.ResponseEntity;


@RestController
@RequiredArgsConstructor
@RequestMapping("/inventories")
@Tag(name = "재고 관리 기능")
public class InventoryController {
    private final InventoryService inventoryService;

    //재고 등록
    @Operation(
            summary = "재고 등록",
            description = "농부의 각 농장에 있는 작물들에 대한 예측 재고 등록 : 예측 수확일, 예측 수확량, 최대 예측 수확량, 작물 아이디")
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Object>> register(@Valid @RequestBody InventoryDto.Register dto) {
        inventoryService.save(dto);
        return ResponseEntity.ok(BaseResponse.successMessage("재고 등록 성공"));
    }

    //재고 수정
    @PostMapping("/update")
    public ResponseEntity<BaseResponse<Object>> update(@RequestBody InventoryDto.Register dto) {
        inventoryService.save(dto);
        return ResponseEntity.ok(BaseResponse.successMessage("재고 수정 성공"));
    }

    //재고 상세 조회
    @GetMapping("/details")
    public ResponseEntity<BaseResponse<CropDto.CropResponse>> details(Long cropId) {
        CropDto.CropResponse result = inventoryService.details(cropId);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    //재고 목록 조회
    @Operation(
            summary = "재고 목록 조회",
            description = "param으로 농장 아이디를 전달 받아 농부가 소유한 농장의 재고 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<FarmDto.FarmResponse>> list(Long farmId) {
        FarmDto.FarmResponse result = inventoryService.list(farmId);
        return ResponseEntity.ok(BaseResponse.success(result));
    }
}
