package org.example.groworders.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.groworders.domain.chat.model.dto.ChatDto;
import org.example.groworders.domain.chat.model.entity.ChatMessage;
import org.example.groworders.domain.chat.service.ChatService;
import org.example.groworders.domain.users.model.dto.UserDto;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(
            Principal principal,
            @DestinationVariable String roomId,
            @Payload String message) {

        if (!(principal instanceof Authentication authentication)) {
            System.out.println("WARN: 인증 없는 사용자 메시지 시도");
            return; // 인증 없으면 처리 중단
        }

        Object principalObj = authentication.getPrincipal();
        if (!(principalObj instanceof UserDto.AuthUser authUser)) {
            System.out.println("WARN: 인증 principal 타입 불일치");
            return;
        }

        // DTO 생성
        ChatDto.Message chatMessageDto = ChatDto.Message.fromAuthUser(authUser, message, roomId);

        // DB 저장
        ChatDto.Message savedMessage = chatService.saveMessage(chatMessageDto);

        // WebSocket 브로드캐스트
        messagingTemplate.convertAndSend("/topic/" + roomId, savedMessage);
    }
}
