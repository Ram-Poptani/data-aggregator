package org.binance.webconsumer.config;


import org.binance.webconsumer.services.RabbitMQService;
import org.binance.webconsumer.services.WebSocketHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import tools.jackson.databind.json.JsonMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "binance")
@Setter
public class WebSocketConfig {

    private String webSocketUrl;
    private String apiKey = "";
    private List<String> symbols;

    @Value("${rabbit-mq.consumer.routing-key:trade.key}")
    private String routingKey;

    @Bean
    public WebSocketConnectionManager binanceConnectionManager(
        JsonMapper jsonMapper, 
        RabbitMQService rabbitMQService
    ) {

        WebSocketClient client = new StandardWebSocketClient();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-MBX-APIKEY", this.apiKey);

        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                client,
                new WebSocketHandler(jsonMapper, rabbitMQService, routingKey),
                webSocketUrl + "?streams=" + String.join("/", this.symbols)
        );

        manager.setHeaders(headers);
        manager.setAutoStartup(true);

        return manager;
    }
}
