package com.ourdressingtable.notification.service;

import com.ourdressingtable.notification.domain.ScheduledNotification;

public interface NotificationSenderService {
    void deliver(ScheduledNotification scheduledNotification);

}
