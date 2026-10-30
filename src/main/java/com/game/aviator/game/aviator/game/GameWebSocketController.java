
package com.game.aviator.game.aviator.game;

import com.game.aviator.game.GameRoundService;
import com.game.aviator.game.dto.GameRoundResponse;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GameWebSocketController {

    private final GameRoundService gameRoundService;

    public GameWebSocketController(
            GameRoundService gameRoundService) {
        this.gameRoundService = gameRoundService;
    }

    @SubscribeMapping("/game/current")
    public GameRoundResponse getCurrentRound() {
        return gameRoundService.getCurrentRound();
    }
}
