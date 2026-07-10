package com.joaopaulofg.personcrud.log.consumer;

import com.joaopaulofg.personcrud.log.dto.RequestLogMessage;
import com.joaopaulofg.personcrud.log.model.RequestLog;
import com.joaopaulofg.personcrud.log.repository.RequestLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestLogConsumer {

    private static final Logger log = LoggerFactory.getLogger(RequestLogConsumer.class);

    private final RequestLogRepository requestLogRepository;

    @RabbitListener(queues = "${app.messaging.request-log-queue}")
    public void consume(RequestLogMessage message) {
        RequestLog requestLog = RequestLog.builder()
                .requestTime(message.requestTime())
                .responseTime(message.responseTime())
                .durationMs(message.durationMs())
                .method(message.method())
                .path(message.path())
                .queryString(message.queryString())
                .statusCode(message.statusCode())
                .remoteAddress(message.remoteAddress())
                .userAgent(message.userAgent())
                .authenticatedUser(message.authenticatedUser())
                .headers(message.headers())
                .build();

        requestLogRepository.save(requestLog);

        log.info(
                "Request log salvo: method={}, path={}, status={}, durationMs={}",
                message.method(),
                message.path(),
                message.statusCode(),
                message.durationMs()
        );
    }
}