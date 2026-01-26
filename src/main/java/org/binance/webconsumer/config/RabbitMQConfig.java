package org.binance.webconsumer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RabbitMQConfig {

    @Value("${rabbit-mq.default.exchange:binance.webconsumer.exchange}")
    private String exchange;

    @Value("${rabbit-mq.consumer.queue:binance.webconsumer.queue}")
    private String queue;

    @Value("${rabbit-mq.consumer.routing-key:binance.webconsumer.key}")
    private String routingKey;


    @Bean
    public FanoutExchange exchange() {
        log.info("Creating RabbitMQ Exchange: {}", exchange);
        return new FanoutExchange(exchange, true, false);
    }


}
