package com.joaopaulofg.personcrud.log.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record RequestLogMessage(
        LocalDateTime requestTime,
        LocalDateTime responseTime,
        long durationMs,
        String method,
        String path,
        String queryString,
        int statusCode,
        String remoteAddress,
        String userAgent,
        String authenticatedUser,
        Map<String, String> headers
) {
}