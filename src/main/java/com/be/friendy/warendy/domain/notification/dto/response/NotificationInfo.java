package com.be.friendy.warendy.domain.notification.dto.response;

import com.be.friendy.warendy.domain.notification.entity.Notification;
import lombok.Builder;

@Builder
public record NotificationInfo(
        String content

) {
    public static NotificationInfo fromEntity(Notification notification) {
        return NotificationInfo.builder()
                .content(notification.getContent())
                .build();
    }
}
