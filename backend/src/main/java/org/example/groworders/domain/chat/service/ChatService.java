package org.example.groworders.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.example.groworders.domain.chat.model.dto.ChatDto;
import org.example.groworders.domain.chat.model.entity.ChatMessage;
import org.example.groworders.domain.chat.model.entity.ChatRoom;
import org.example.groworders.domain.chat.repository.ChatMessageRepository;
import org.example.groworders.domain.chat.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 기존 메시지 저장
    @Transactional
    public ChatDto.Message saveMessage(ChatDto.Message dto) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        ChatMessage entity = dto.toEntity(chatRoom);
        ChatMessage saved = chatMessageRepository.save(entity);

        // Entity → DTO 변환 후 반환
        return ChatDto.Message.fromEntity(saved);
    }

    // ✅ 1:1 채팅방 생성 또는 기존 조회
    @Transactional
    public ChatDto.Room getOrCreateRoom(Long buyerId, Long farmerId) {
        // buyer-seller 조합으로 roomId 검색
        String roomKey = generateRoomKey(buyerId, farmerId);

        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomKey)
                .orElseGet(() -> {
                    // 없으면 새 채팅방 생성
                    ChatRoom newRoom = ChatRoom.builder()
                            .roomId(roomKey)
                            .buyerId(buyerId)
                            .farmerId(farmerId)
                            .build();
                    return chatRoomRepository.save(newRoom);
                });

        return ChatDto.Room.fromEntity(chatRoom);
    }

    // 고유 roomId 생성 (UUID 사용)
    private String generateRoomKey(Long buyerId, Long farmerId) {
        // buyerId < sellerId 정렬 → 항상 같은 key
        Long first = Math.min(buyerId, farmerId);
        Long second = Math.max(buyerId, farmerId);
        return first + "_" + second;
    }

    // 특정 채팅방 메시지 조회
    @Transactional(readOnly = true)
    public List<ChatDto.Message> getMessagesByRoomId(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        return chatRoom.getMessages().stream()
                .map(ChatDto.Message::fromEntity)
                .collect(Collectors.toList());
    }

    // 구매자가 참여한 모든 채팅방 조회
    @Transactional(readOnly = true)
    public List<ChatDto.Room> getRoomsByBuyer(Long buyerId) {
        return chatRoomRepository.findAllByBuyerId(buyerId).stream()
                .map(ChatDto.Room::fromEntity)
                .collect(Collectors.toList());
    }

    // 농부가 참여한 모든 채팅방 조회
    @Transactional(readOnly = true)
    public List<ChatDto.Room> getRoomsByFarmer(Long farmerId) {
        return chatRoomRepository.findAllByFarmerId(farmerId).stream()
                .map(ChatDto.Room::fromEntity)
                .collect(Collectors.toList());
    }

}
