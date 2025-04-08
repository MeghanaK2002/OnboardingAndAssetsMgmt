package org.meghana.OnboardingAndAssetsMgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisInvalidatedTokenService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String INVALIDATED_TOKEN_PREFIX = "invalidated:";
    private static final long INVALIDATED_TOKEN_TTL_SECONDS = 3600; // Keep invalidated tokens for the same duration as JWT

    public void invalidateToken(String token) {
        redisTemplate.opsForValue().set(INVALIDATED_TOKEN_PREFIX + token, "invalidated", INVALIDATED_TOKEN_TTL_SECONDS, TimeUnit.SECONDS);
    }

    public boolean isTokenInvalidated(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(INVALIDATED_TOKEN_PREFIX + token));
    }
}