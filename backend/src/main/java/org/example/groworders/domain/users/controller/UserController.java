package org.example.groworders.domain.users.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.groworders.common.model.BaseResponse;
import org.example.groworders.domain.users.model.dto.UserDto;
import org.example.groworders.domain.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<Void>> signup(@RequestBody UserDto.SignUp dto) throws MessagingException {
        userService.signup(dto);
        return ResponseEntity.ok(BaseResponse.successMessage("등록 성공"));
    }

    @GetMapping("/verify")
    public ResponseEntity<BaseResponse<Void>> verify(@RequestParam String uuid) {
        userService.verify(uuid);
        return ResponseEntity.ok(BaseResponse.successMessage("등록 성공"));
    }
}
