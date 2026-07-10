package com.joaopaulofg.personcrud.log.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "request_logs")
public class RequestLog {

    @Id
    private String id;

    private LocalDateTime requestTime;
    private LocalDateTime responseTime;
    private long durationMs;

    private String method;
    private String path;
    private String queryString;
    private int statusCode;

    private String remoteAddress;
    private String userAgent;
    private String authenticatedUser;

    private Map<String, String> headers;

    @CreatedDate
    private LocalDateTime createdAt;
}