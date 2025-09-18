package com.ourdressingtable.notification.service;

import com.ourdressingtable.membercosmetic.domain.MemberCosmetic;
import com.ourdressingtable.notification.domain.NotificationType;

import java.time.LocalDate;

public interface NotificationScheduleService {
    void scheduleNotification(MemberCosmetic memberCosmetic);
    void scheduleOne(Long memberId, Long cosmeticId, LocalDate localDateKst, NotificationType type);
    void cancel(Long notificationId);
}
