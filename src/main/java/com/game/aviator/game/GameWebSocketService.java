package com.game.aviator.game;

import com.game.aviator.game.dto.GameRoundResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public GameWebSocketService(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastGameUpdate(GameRoundResponse game) {

        messagingTemplate.convertAndSend(
                "/topic/game/current",
                game
        );
//        System.out.println("WEBSOCKET BROADCAST SENT: " + next);
    }

}