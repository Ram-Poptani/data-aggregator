package org.binance.webconsumer.config;


import org.binance.webconsumer.services.WebSocketHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "binance")
@Setter
public class WebSocketConfig {

    private String webSocketUrl;
    private String apiKey = "";
    private List<String> symbols;

    @Bean
    public WebSocketConnectionManager binanceConnectionManager(ObjectMapper objectMapper) {

        WebSocketClient client = new StandardWebSocketClient();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-MBX-APIKEY", this.apiKey);

        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                client,
                new WebSocketHandler(objectMapper),
                webSocketUrl + "?streams=" + String.join("/", this.symbols)
        );

        manager.setHeaders(headers);
        manager.setAutoStartup(true);

        return manager;
    }
}
