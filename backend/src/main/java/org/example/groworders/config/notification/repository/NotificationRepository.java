package org.example.groworders.config.notification.repository;

import org.example.groworders.config.notification.model.entity.NotificationSub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationSub, Long>{
}
