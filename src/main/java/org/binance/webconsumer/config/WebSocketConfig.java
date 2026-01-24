package org.binance.webconsumer.config;


import org.binance.webconsumer.services.WebSocketHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "binance")
public class WebSocketConfig {

    private String webSocketUrl;
    private String apiKey = "";
    private List<String> symbols;

    // Setters required for @ConfigurationProperties binding
    public void setWebSocketUrl(String webSocketUrl) { this.webSocketUrl = webSocketUrl; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public void setSymbols(List<String> symbols) { this.symbols = symbols; }

    @Bean
    public WebSocketConnectionManager binanceConnectionManager() {

        WebSocketClient client = new StandardWebSocketClient();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-MBX-APIKEY", apiKey);

        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                client,
                new WebSocketHandler(),
                webSocketUrl + "?streams=" + String.join("/", symbols)
        );

        manager.setHeaders(headers);
        manager.setAutoStartup(true);

        return manager;
    }
}
