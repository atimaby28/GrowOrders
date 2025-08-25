package org.example.groworders.config.notification.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class NotificationKeys {
    @Column(nullable = false)
    private String p256dh;

    @Column(nullable = false)
    private String auth;
}
