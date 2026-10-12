package com.example.aviator.game;

import com.example.aviator.game.dto.GameRoundResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/game")
//@RequestMapping(.requestMatchers("/api/game/**").permitAll())
public class GameRoundController {

    private final GameRoundService gameRoundService;

    public GameRoundController(GameRoundService gameRoundService) {
        this.gameRoundService = gameRoundService;
    }

    @PostMapping("/rounds")
    public ResponseEntity<GameRoundResponse> createRound() {

        return ResponseEntity.ok(
                gameRoundService.createRound()
        );
    }

    @PostMapping("/rounds/{id}/start")
    public ResponseEntity<GameRoundResponse> startRound(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                gameRoundService.startRound(id)
        );
    }

    @PostMapping("/rounds/{id}/crash")
    public ResponseEntity<GameRoundResponse> crashRound(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                gameRoundService.crashRound(id)
        );
    }

    @GetMapping("/rounds/{id}")
    public ResponseEntity<GameRoundResponse> getRound(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                gameRoundService.getRound(id)
        );
    }

    @PostMapping("/rounds/{id}/multiplier")
    public ResponseEntity<GameRoundResponse> updateMultiplier(
            @PathVariable Long id,
            @RequestParam BigDecimal multiplier
    ) {

        return ResponseEntity.ok(
                gameRoundService.updateMultiplier(
                        id,
                        multiplier
                )
        );
    }

    @GetMapping("/current")
    public ResponseEntity<GameRoundResponse> getCurrentRound() {

        return ResponseEntity.ok(
                gameRoundService.getCurrentRound()
        );
    }
}