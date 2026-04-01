package com.example.util;

import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

// 만료된 remember-me 토큰을 주기적으로 삭제
@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final JdbcTemplate jdbcTemplate;

    // 매일 새벽 3시에 7일 이상 지난 토큰 삭제
    @Scheduled(cron = "0 0 3 * * *")
    public void deleteExpiredTokens() {
        jdbcTemplate.update(
                "DELETE FROM persistent_logins WHERE last_used < ?",
                new Date(System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000))
        );
    }
}
