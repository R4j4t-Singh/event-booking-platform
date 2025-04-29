package com.example.event_booking_platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LockService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean acquireLock(String key, String value, long ttl) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, value, ttl, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }

    public void releaseLock(String key, String value) {
        String currentValue = redisTemplate.opsForValue().get(key);
        if(value.equals(currentValue)) {
            redisTemplate.delete(key);
        }
    }
}
