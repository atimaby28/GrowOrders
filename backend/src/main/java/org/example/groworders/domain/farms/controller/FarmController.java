package org.example.groworders.domain.farms.controller;

import lombok.RequiredArgsConstructor;
import org.example.groworders.common.model.BaseResponse;
import org.example.groworders.domain.farms.model.dto.FarmDto;
import org.example.groworders.domain.farms.service.FarmService;
import org.example.groworders.domain.users.model.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


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
//private Farm crop;

@RestController
@RequiredArgsConstructor
@RequestMapping("/farms")
public class FarmController {

    private final FarmService farmservice;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Void>> register(@RequestBody FarmDto.Register dto,
                                                       @AuthenticationPrincipal UserDto.AuthUser authUser) {
        farmservice.register(dto, authUser.getId());
        return ResponseEntity.ok(BaseResponse.successMessage("농장 등록 성공"));
    }

    @GetMapping("/list")
    public ResponseEntity<BaseResponse<Void>> list() {
        farmservice.listAll();
        return ResponseEntity.ok(BaseResponse.successMessage("농장 출력 성공"));
    }
}
