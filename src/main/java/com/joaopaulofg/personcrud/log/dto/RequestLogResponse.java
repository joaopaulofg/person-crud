package com.joaopaulofg.personcrud.log.dto;

import java.time.LocalDateTime;

public record RequestLogResponse(
        String id,
        LocalDateTime requestTime,
        LocalDateTime responseTime,
        long durationMs,
        String method,
        String path,
        String queryString,
        int statusCode,
        String remoteAddress,
        String userAgent,
        String authenticatedUser
) {
}