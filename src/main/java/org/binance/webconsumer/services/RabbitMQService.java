package org.binance.webconsumer.services;


import java.io.Serializable;
import java.time.Instant;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.Getter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RabbitMQService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbit-mq.default.exchange}")
    private String defaultExchange;

//    Metrics
    private final Counter tradesSentCounter;
    private final Counter publishErrorCounter;
    private final Timer publishTimer;

    @Getter
    private volatile Instant lastSentTime;

    public RabbitMQService(
                            RabbitTemplate rabbitTemplate,
                            MeterRegistry meterRegistry
                            ) {
        this.rabbitTemplate = rabbitTemplate;
        this.tradesSentCounter = meterRegistry.counter("rabbitmq.messages.trades.sent");
        this.publishErrorCounter = meterRegistry.counter("rabbitmq.publish.errors");
        this.publishTimer = meterRegistry.timer("rabbitmq.messages.publish.time");

    }

    public void sendMessage(String exchange, String routingKey, Serializable message) {
        try {
            log.debug("Sending message to RabbitMQ - Exchange: {}, Routing Key: {}", exchange, routingKey);
            publishTimer.record(() -> {
                rabbitTemplate.convertAndSend(exchange, routingKey, message);
            });
            log.debug("Message sent successfully to exchange: {}, routingKey: {}", exchange, routingKey);
            this.tradesSentCounter.increment();
            this.lastSentTime = Instant.now();
        } catch (Exception e) {
            // TODO: Catch specific exceptions related to RabbitMQ and add retry mechanism if needed
            log.error("Failed to send message to RabbitMQ - Exchange: {}, Routing Key: {}", exchange, routingKey, e);
            this.publishErrorCounter.increment();
            throw new RuntimeException("Failed to send message to RabbitMQ", e);
        }
    }

    public void sendMessage(String routingKey, Serializable message) {
        this.sendMessage(defaultExchange, routingKey, message);
    }
}
