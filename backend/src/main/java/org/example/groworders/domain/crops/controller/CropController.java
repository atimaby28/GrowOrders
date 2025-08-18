package org.example.groworders.domain.crops.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.groworders.common.model.BaseResponse;
import org.example.groworders.domain.crops.model.dto.CropDto;
import org.example.groworders.domain.crops.service.CropService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crops")
@Tag(name = "작물 관리 기능")
public class CropController {
    private final CropService cropService;

    //작물 등록
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Object>> register(@RequestBody CropDto.Register dto) {
        cropService.register(dto);
        return ResponseEntity.ok(BaseResponse.successMessage("작물 등록 성공"));
    }
}
