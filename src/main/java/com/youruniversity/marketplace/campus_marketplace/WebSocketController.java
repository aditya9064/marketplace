package com.youruniversity.marketplace.campus_marketplace;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class WebSocketController {
    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public String handleMessage(String message) {
        log.info("Received message: {}", message);
        return message;
    }
}
