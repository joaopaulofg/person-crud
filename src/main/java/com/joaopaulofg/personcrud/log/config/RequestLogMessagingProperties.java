package com.joaopaulofg.personcrud.log.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.messaging")
public record RequestLogMessagingProperties(
        String requestLogExchange,
        String requestLogQueue,
        String requestLogRoutingKey
) {
}