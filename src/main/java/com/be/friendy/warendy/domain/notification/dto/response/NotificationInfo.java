package com.be.friendy.warendy.domain.notification.dto.response;

import com.be.friendy.warendy.domain.member.dto.response.InfoResponse;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.notification.entity.Notification;
import lombok.Builder;

@Builder
public record NotificationInfo(
        boolean isRead,
        String content,
        Member receiver
) {
    public static NotificationInfo fromEntity(Notification notification) {
        return NotificationInfo.builder()
                .isRead(notification.isRead())
                .content(notification.getContent())
                .receiver(notification.getReceiver())
                .build();
    }
}
