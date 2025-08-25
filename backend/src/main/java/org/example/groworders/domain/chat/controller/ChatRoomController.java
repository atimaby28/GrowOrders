package org.example.groworders.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.groworders.domain.chat.model.dto.ChatDto;
import org.example.groworders.domain.chat.model.dto.ChatDto.Room;
import org.example.groworders.domain.chat.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/room")
public class ChatRoomController {

    private final ChatService chatService;

    // 1:1 채팅방 생성 또는 기존 조회
    @PostMapping("/get-or-create")
    public ChatDto.Room getOrCreateRoom(@RequestBody ChatDto.Room request) {
        return chatService.getOrCreateRoom(request.getBuyerId(), request.getFarmerId());
    }

    // 특정 채팅방 메시지 히스토리 조회
    @GetMapping("/{roomId}/messages")
    public List<ChatDto.Message> getMessages(@PathVariable String roomId) {
        return chatService.getMessagesByRoomId(roomId);
    }

    // 구매자가 참여한 채팅방 리스트 조회
    @GetMapping("/buyer/{buyerId}")
    public List<Room> getRoomsByBuyer(@PathVariable Long buyerId) {
        return chatService.getRoomsByBuyer(buyerId);
    }

    // 농부가 참여한 채팅방 리스트 조회
    @GetMapping("/farmer/{farmerId}")
    public List<Room> getRoomsByFarmer(@PathVariable Long farmerId) {
        return chatService.getRoomsByFarmer(farmerId);
    }
}
