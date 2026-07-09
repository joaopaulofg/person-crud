package com.joaopaulofg.personcrud.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record AppSecurityProperties(
        String apiKey,
        String jwtSecret,
        long jwtExpirationMinutes
) {
}