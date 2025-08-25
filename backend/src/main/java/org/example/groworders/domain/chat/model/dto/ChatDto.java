package org.example.groworders.domain.chat.model.dto;

import lombok.*;
import org.example.groworders.domain.chat.model.entity.ChatMessage;
import org.example.groworders.domain.chat.model.entity.ChatRoom;
import org.example.groworders.domain.users.model.dto.UserDto;

public class ChatDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        private Long senderId;
        private String senderName;
        private String message;
        private String roomId;

        // AuthUser → DTO 생성
        public static Message fromAuthUser(UserDto.AuthUser authUser, String message, String roomId) {
            return Message.builder()
                    .senderId(authUser.getId())
                    .senderName(authUser.getName())
                    .message(message)
                    .roomId(roomId)
                    .build();
        }

        // DTO → Entity 변환
        public ChatMessage toEntity(ChatRoom chatRoom) {
            return ChatMessage.builder()
                    .message(this.message)
                    .roomId(this.roomId)
                    .senderId(this.senderId)
                    .senderName(this.senderName)
                    .chatRoom(chatRoom)
                    .build();
        }

        // Entity → DTO 변환 (DB 조회 시 사용)
        public static Message fromEntity(ChatMessage entity) {
            return Message.builder()
                    .senderId(entity.getSenderId())
                    .senderName(entity.getSenderName())
                    .message(entity.getMessage())
                    .roomId(entity.getRoomId())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Room {
        private String roomId;
        private Long buyerId;
        private Long farmerId;

        // Entity → DTO
        public static Room fromEntity(ChatRoom chatRoom) {
            return Room.builder()
                    .roomId(chatRoom.getRoomId())
                    .buyerId(chatRoom.getBuyerId())
                    .farmerId(chatRoom.getFarmerId())
                    .build();
        }
    }


}
