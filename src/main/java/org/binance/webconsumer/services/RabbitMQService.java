package org.binance.webconsumer.services;


import java.io.Serializable;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMQService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbit-mq.default.exchange}")
    private String defaultExchange;

    public void sendMessage(String exchange, String routingKey, Serializable message) {
        try {
            log.debug("Sending message to RabbitMQ - Exchange: {}, Routing Key: {}", exchange, routingKey);
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            log.debug("Message sent successfully to exchange: {}, routingKey: {}", exchange, routingKey);
        } catch (Exception e) {
            // TODO: Catch specific exceptions related to RabbitMQ and add retry mechanism if needed
            log.error("Failed to send message to RabbitMQ - Exchange: {}, Routing Key: {}", exchange, routingKey, e);
            throw new RuntimeException("Failed to send message to RabbitMQ", e);
        }
    }

    public void sendMessage(String routingKey, Serializable message) {
        this.sendMessage(defaultExchange, routingKey, message);
    }

}
