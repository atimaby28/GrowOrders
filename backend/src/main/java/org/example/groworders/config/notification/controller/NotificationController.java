package org.example.groworders.config.notification.controller;

import lombok.RequiredArgsConstructor;
import org.example.groworders.config.notification.model.dto.NotificationDto;
import org.example.groworders.config.notification.service.NotificationService;
import org.example.groworders.config.notification.model.entity.NotificationSub;
import org.example.groworders.domain.users.model.dto.UserDto;
import org.jose4j.lang.JoseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/push")
@CrossOrigin(originPatterns = "*")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/sub")
    public ResponseEntity sub(@RequestBody NotificationDto.Subscribe dto,
                              @AuthenticationPrincipal UserDto.AuthUser authUser) {
        notificationService.subscribe(dto, authUser.getId());
        return ResponseEntity.status(200).body("구독 성공");
    }

    @PostMapping("/send")
    public ResponseEntity send(@RequestBody NotificationSub dto) throws JoseException, GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        notificationService.send(dto);
        return ResponseEntity.status(200).body("전송 성공");
    }
}
