package com.ourdressingtable.notification.service;

import com.ourdressingtable.notification.component.KafkaNotificationProducer;
import com.ourdressingtable.notification.domain.NotificationStatus;
import com.ourdressingtable.notification.repository.ScheduledNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class NotificationWorker {
    private final StringRedisTemplate stringRedisTemplate;
    private final ScheduledNotificationRepository scheduledNotificationRepository;
    private final KafkaNotificationProducer producer;

    private static final String ZSET_KEY = "notifications";

    @Value("${notification.pop.batch-size:200}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${notification.worker.fixed-delay-ms:1000}")
    @Transactional
    public void schedule() {
        long now = System.currentTimeMillis();
        Set<String> due = stringRedisTemplate.opsForZSet().rangeByScore(ZSET_KEY, 0, now, 0, batchSize);
        if (due == null || due.isEmpty()) return;

        for (String key : due) {
            Long removed = stringRedisTemplate.opsForZSet().remove(ZSET_KEY, key);
            if (removed != null && removed > 0) {
                String dedup = "notification"+key;
                Boolean first = stringRedisTemplate.opsForValue().setIfAbsent(dedup, "1", Duration.ofMinutes(10));
                if (Boolean.TRUE.equals(first)) {
                    producer.send(key);
                    scheduledNotificationRepository.findById(Long.valueOf(key)).ifPresent(n -> { n.markEnqueued(); scheduledNotificationRepository.save(n); });
                }
            }
        }
    }
}
