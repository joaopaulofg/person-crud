package com.joaopaulofg.personcrud.log.filter;

import com.joaopaulofg.personcrud.log.dto.RequestLogMessage;
import com.joaopaulofg.personcrud.log.producer.RequestLogProducer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Set<String> SENSITIVE_HEADERS = Set.of(
            "authorization",
            "cookie",
            "set-cookie"
    );

    private final RequestLogProducer requestLogProducer;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        LocalDateTime requestTime = LocalDateTime.now();
        long start = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            LocalDateTime responseTime = LocalDateTime.now();
            long durationMs = System.currentTimeMillis() - start;

            RequestLogMessage message = new RequestLogMessage(
                    requestTime,
                    responseTime,
                    durationMs,
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getQueryString(),
                    response.getStatus(),
                    request.getRemoteAddr(),
                    request.getHeader("User-Agent"),
                    getAuthenticatedUser(),
                    extractSafeHeaders(request)
            );

            requestLogProducer.publish(message);
        }
    }

    private String getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return authentication.getName();
    }

    private Map<String, String> extractSafeHeaders(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
                .stream()
                .filter(headerName -> !SENSITIVE_HEADERS.contains(headerName.toLowerCase()))
                .collect(Collectors.toMap(
                        headerName -> headerName,
                        request::getHeader
                ));
    }
}