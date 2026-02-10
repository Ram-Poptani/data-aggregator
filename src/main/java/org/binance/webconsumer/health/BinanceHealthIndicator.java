package org.binance.webconsumer.health;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketConnectionManager;

@Component
public class BinanceHealthIndicator implements HealthIndicator {

    private final WebSocketConnectionManager connectionManager;
    private final String webSocketUrl;

    public BinanceHealthIndicator(
            WebSocketConnectionManager connectionManager,
            @Value("${binance.web-socket-url}") String webSocketUrl
    ) {
        this.connectionManager = connectionManager;
        this.webSocketUrl = webSocketUrl;
    }

    @Override
    public Health health() {
        boolean isConnected = connectionManager.isRunning();

        if (isConnected) {
            return Health.up()
                    .withDetail("binanceWebSocket", "Connected")
                    .withDetail("url", webSocketUrl)
                    .build();
        } else {
            return Health.down()
                    .withDetail("binanceWebSocket", "Disconnected")
                    .withDetail("url", webSocketUrl)
                    .build();
        }
    }
}