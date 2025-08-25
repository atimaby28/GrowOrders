package org.example.groworders.config.notification.model.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.groworders.config.notification.model.entity.NotificationKeys;
import org.example.groworders.config.notification.model.entity.NotificationSub;

public class NotificationDto {
    @Getter
    @Builder
    public static class Subscribe {
        private String endpoint;
        private NotificationKeys keys;
        private Long userId;

        public NotificationSub toEntity() {
            NotificationSub entity = NotificationSub.builder()
                    .endpoint(endpoint)
                    .keys(keys)
                    .userId(userId)
                    .build();
            return entity;
        }
    }

    @Getter
    @Builder
    public static class Send {
        private NotificationSub sub;


    }
}
