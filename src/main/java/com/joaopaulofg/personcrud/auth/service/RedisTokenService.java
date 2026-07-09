package com.joaopaulofg.personcrud.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private static final String TOKEN_PREFIX = "auth:token:";

    private final StringRedisTemplate redisTemplate;

    public void save(String tokenId, String subject, long expiresInSeconds) {
        String key = buildKey(tokenId);

        redisTemplate.opsForValue().set(
                key,
                subject,
                Duration.ofSeconds(expiresInSeconds)
        );
    }

    public boolean exists(String tokenId) {
        String key = buildKey(tokenId);

        Boolean exists = redisTemplate.hasKey(key);

        return Boolean.TRUE.equals(exists);
    }

    public void delete(String tokenId) {
        String key = buildKey(tokenId);

        redisTemplate.delete(key);
    }

    private String buildKey(String tokenId) {
        return TOKEN_PREFIX + tokenId;
    }
}