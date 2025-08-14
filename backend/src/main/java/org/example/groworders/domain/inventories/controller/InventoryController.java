package org.example.groworders.domain.inventories.controller;

import lombok.*;
import org.example.groworders.common.model.BaseResponse;
import org.example.groworders.domain.farms.model.dto.FarmDto;
import org.springframework.web.bind.annotation.*;
import org.example.groworders.domain.inventories.model.dto.InventoryDto;
import org.example.groworders.domain.inventories.service.InventoryService;
import org.springframework.http.ResponseEntity;


@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    //재고 등록
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Object>> register(@RequestBody InventoryDto.Register dto) {
        inventoryService.register(dto);
        return ResponseEntity.ok(BaseResponse.successMessage("재고 등록 성공"));
    }

    //재고 상세 조회
    @GetMapping("/details")
    public ResponseEntity details() {
        return null;
    }

    //재고 목록 조회
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<FarmDto.FarmRes>> list(Long farmId) {
        FarmDto.FarmRes result = inventoryService.list(farmId);
        return ResponseEntity.ok(BaseResponse.success(result));
    }
}
