package com.joaopaulofg.personcrud.log.producer;

import com.joaopaulofg.personcrud.log.config.RequestLogMessagingProperties;
import com.joaopaulofg.personcrud.log.dto.RequestLogMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestLogProducer {

    private static final Logger log = LoggerFactory.getLogger(RequestLogProducer.class);

    private final RabbitTemplate rabbitTemplate;
    private final RequestLogMessagingProperties properties;

    public void publish(RequestLogMessage message) {
        try {
            rabbitTemplate.convertAndSend(
                    properties.requestLogExchange(),
                    properties.requestLogRoutingKey(),
                    message
            );
        } catch (Exception exception) {
            log.error("Erro ao publicar log de requisição no RabbitMQ", exception);
        }
    }
}