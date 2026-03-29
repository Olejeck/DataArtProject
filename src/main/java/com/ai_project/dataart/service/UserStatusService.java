package com.ai_project.dataart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserStatusService {
    private final StringRedisTemplate redisTemplate;
    private static final String STATUS_KEY_PREFIX = "user:status:";

    public void setUserOnline(String username) {
        // Зберігаємо статус "ONLINE" на 5 хвилин (з оновленням при активності)
        redisTemplate.opsForValue().set(STATUS_KEY_PREFIX + username, "ONLINE", 5, TimeUnit.MINUTES);
    }

    public void setUserOffline(String username) {
        redisTemplate.delete(STATUS_KEY_PREFIX + username);
    }

    public String getUserStatus(String username) {
        String status = redisTemplate.opsForValue().get(STATUS_KEY_PREFIX + username);
        return (status != null) ? status : "OFFLINE";
    }
}