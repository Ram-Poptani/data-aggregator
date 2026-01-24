package org.binance.webconsumer.services;


import lombok.AllArgsConstructor;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@AllArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final JsonMapper mapper;
    private final RabbitMQService rabbitMQService;
    private final String routingKey;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Connected to WebSocket");
    }

    @Override
    protected void handleTextMessage(
            WebSocketSession session,
            TextMessage message) throws Exception {

        JsonNode json = mapper.readTree(message.getPayload());
        rabbitMQService.sendMessage(routingKey, json.toString());
    }

    @Override
    public void handleTransportError(
            WebSocketSession session,
            Throwable exception) {
        log.error("Error: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(
            WebSocketSession session,
            CloseStatus status) {
        log.info("Connection closed: " + status);
    }
}
