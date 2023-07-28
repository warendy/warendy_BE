package com.be.friendy.warendy.domain.notification.repository;

import com.be.friendy.warendy.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
