package org.example.groworders.domain.chat.repository;


import org.example.groworders.domain.chat.model.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
