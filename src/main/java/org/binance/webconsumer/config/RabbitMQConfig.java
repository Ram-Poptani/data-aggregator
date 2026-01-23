package org.binance.webconsumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

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
    public TopicExchange exchange() {
        log.info("Creating TopicExchange: {}", exchange);
        return new TopicExchange(exchange, true, false);
    }

    @Bean
    public Queue queue() {
        log.info("Creating Queue: {}", queue);
        return new Queue(queue, true, false, false);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        log.info("Creating Binding: Queue '{}' -> Exchange '{}' with routing key '{}'",
                queue, this.exchange, routingKey);
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
