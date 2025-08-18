package org.example.groworders.domain.farms.controller;

import lombok.RequiredArgsConstructor;
import org.example.groworders.common.model.BaseResponse;
import org.example.groworders.domain.farms.model.dto.FarmDto;
import org.example.groworders.domain.farms.service.FarmService;
import org.example.groworders.domain.users.model.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


//User
//@OneToMany(mappedBy = "user")
//List<Farm> farmList;

//UserRepository
//User getReferenceById(Long userId);

// Crop
//@ManyToOne
//@JoinColumn(name = "crop")
//private Farm farm;

//@ManyToOne
//@JoinColumn(name = "crop")
//private Crop crop;

@RestController
@RequiredArgsConstructor
@RequestMapping("/farms")
public class FarmController {

    private final FarmService farmservice;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<FarmDto.FarmResponse>> register(
            @RequestPart("dto") FarmDto.Register dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        FarmDto.FarmResponse result = farmservice.register(dto, image);
        return ResponseEntity.ok(BaseResponse.success(result));
    }


//    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<BaseResponse<FarmDto.FarmResponse>> register(
//            @RequestPart("dto") FarmDto.Register dto,
//            @RequestPart(value = "image", required = false) MultipartFile image,
//            @AuthenticationPrincipal UserDto.AuthUser authUser) {
//        FarmDto.FarmResponse result = farmservice.register(dto, image, authUser.getId());
//        return ResponseEntity.ok(BaseResponse.success(result));
//    }

    @GetMapping("/list")
    public ResponseEntity<BaseResponse<List<FarmDto.FarmResponse>>> list() {
        List<FarmDto.FarmResponse> result = farmservice.listAll();
        return ResponseEntity.ok(BaseResponse.success(result));
    }
}
