package com.ourdressingtable.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourdressingtable.membercosmetic.domain.MemberCosmetic;
import com.ourdressingtable.membercosmetic.repository.MemberCosmeticRepository;
import com.ourdressingtable.notification.domain.NotificationStatus;
import com.ourdressingtable.notification.domain.NotificationType;
import com.ourdressingtable.notification.domain.ScheduledNotification;
import com.ourdressingtable.notification.repository.ScheduledNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationScheduleServiceImpl implements NotificationScheduleService {
    private final ScheduledNotificationRepository scheduledNotificationRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    private static final String ZSET_KEY = "notifications";
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    @Override
    @Transactional
    public void scheduleNotification(MemberCosmetic memberCosmetic) {
        Long memberId = memberCosmetic.getId();
        Long cosmeticId = memberCosmetic.getId();
        LocalDate expiry = memberCosmetic.getExpiredDate();

        scheduleOne(memberId, cosmeticId, expiry.minusMonths(1), NotificationType.COSMETIC_EXPIRY_MONTH_BEFORE);
        scheduleOne(memberId, cosmeticId, expiry.minusWeeks(1), NotificationType.COSMETIC_EXPIRY_WEEK_BEFORE);
        scheduleOne(memberId, cosmeticId, expiry.minusDays(1), NotificationType.COSMETIC_EXPIRY_DAY_BEFORE);
        scheduleOne(memberId, cosmeticId, expiry, NotificationType.COSMETIC_EXPIRY_DAY_OF);

    }

    @Override
    public void scheduleOne(Long memberId, Long cosmeticId, LocalDate localDateKst, NotificationType type) {
        if (localDateKst == null) return;

        ZonedDateTime kst0800 = localDateKst.atTime(8, 0).atZone(KST);
        Instant instant = kst0800.toInstant();

        if(instant.isBefore(Instant.now())) return;

        String payloadJson;
        try {
            payloadJson = objectMapper.writeValueAsString(
                    java.util.Map.of("cosmeticId", cosmeticId, "deepLink", "app://cosmetic/"+cosmeticId)
            );

        } catch (Exception e) {
            payloadJson = "{}";
        }

        var saved = scheduledNotificationRepository.save(ScheduledNotification.builder()
                .memberId(memberId)
                .type(type)
                .payloadJson(payloadJson)
                .notifyAt(instant)
                .timezone("Asia/Seoul")
                .status(NotificationStatus.SCHEDULED)
                .build());

        stringRedisTemplate.opsForZSet().add(ZSET_KEY, String.valueOf(saved.getId()), saved.epochMillis());
    }

    @Override
    @Transactional
    public void cancel(Long notificationId) {
        stringRedisTemplate.opsForZSet().remove(ZSET_KEY, String.valueOf(notificationId));
        scheduledNotificationRepository.findById(notificationId).ifPresent(n -> {
            n.getStatus();
            scheduledNotificationRepository.save(n);
        });
    }
}
