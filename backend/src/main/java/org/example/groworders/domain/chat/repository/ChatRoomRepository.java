package org.example.groworders.domain.chat.repository;

import org.example.groworders.domain.chat.model.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByRoomId(String roomId);

    // 구매자가 참여한 채팅방 조회
    List<ChatRoom> findAllByBuyerId(Long buyerId);

    // 농부가 참여한 채팅방 조회
    List<ChatRoom> findAllByFarmerId(Long farmerId);
}
