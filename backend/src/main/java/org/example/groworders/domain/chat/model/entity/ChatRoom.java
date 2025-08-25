package org.example.groworders.domain.chat.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.groworders.common.model.BaseEntity;
import org.example.groworders.domain.users.model.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId; // UUID or buyer-seller unique key

    private Long buyerId;

    private Long farmerId;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ChatMessage> messages = new ArrayList<>();
}