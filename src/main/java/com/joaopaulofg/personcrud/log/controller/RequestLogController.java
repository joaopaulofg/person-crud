package com.joaopaulofg.personcrud.log.controller;

import com.joaopaulofg.personcrud.log.dto.RequestLogResponse;
import com.joaopaulofg.personcrud.log.service.RequestLogService;
import com.joaopaulofg.personcrud.shared.pagination.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
public class RequestLogController {

    private final RequestLogService requestLogService;

    @GetMapping
    public PageResponse<RequestLogResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return requestLogService.findAll(page, size);
    }
}