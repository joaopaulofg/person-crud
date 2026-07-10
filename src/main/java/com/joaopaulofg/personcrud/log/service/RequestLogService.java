package com.joaopaulofg.personcrud.log.service;

import com.joaopaulofg.personcrud.log.dto.RequestLogResponse;
import com.joaopaulofg.personcrud.log.model.RequestLog;
import com.joaopaulofg.personcrud.log.repository.RequestLogRepository;
import com.joaopaulofg.personcrud.shared.pagination.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestLogService {

    private final RequestLogRepository requestLogRepository;

    public PageResponse<RequestLogResponse> findAll(int page, int size) {
        int normalizedPage = Math.max(page, 0);
        int normalizedSize = size <= 0 ? 10 : Math.min(size, 100);

        PageRequest pageRequest = PageRequest.of(
                normalizedPage,
                normalizedSize,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        var logsPage = requestLogRepository.findAll(pageRequest);

        return new PageResponse<>(
                logsPage.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList(),
                logsPage.getNumber(),
                logsPage.getSize(),
                logsPage.getTotalElements(),
                logsPage.getTotalPages(),
                logsPage.isFirst(),
                logsPage.isLast()
        );
    }

    private RequestLogResponse toResponse(RequestLog requestLog) {
        return new RequestLogResponse(
                requestLog.getId(),
                requestLog.getRequestTime(),
                requestLog.getResponseTime(),
                requestLog.getDurationMs(),
                requestLog.getMethod(),
                requestLog.getPath(),
                requestLog.getQueryString(),
                requestLog.getStatusCode(),
                requestLog.getRemoteAddress(),
                requestLog.getUserAgent(),
                requestLog.getAuthenticatedUser()
        );
    }
}