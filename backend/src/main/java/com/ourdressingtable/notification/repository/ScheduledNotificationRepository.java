package com.ourdressingtable.notification.repository;

import com.ourdressingtable.notification.domain.ScheduledNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduledNotificationRepository extends JpaRepository<ScheduledNotification, Long> {
}
