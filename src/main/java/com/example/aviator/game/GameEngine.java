package com.example.aviator.game;

import com.example.aviator.bet.BetService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class GameEngine {

    private final GameRoundRepository gameRoundRepository;
    private final GameRoundService gameRoundService;

    private final BetService betService;

    public GameEngine(
            GameRoundRepository gameRoundRepository,
            GameRoundService gameRoundService,
            BetService betService
    ) {
        this.gameRoundRepository = gameRoundRepository;
        this.gameRoundService = gameRoundService;
        this.betService = betService;
    }

    @Scheduled(fixedRate = 100)
    public void updateGame() {

        GameRound round =
                gameRoundRepository
                        .findFirstByStatusOrderByIdDesc(
                                GameStatus.RUNNING
                        )
                        .orElse(null);

        if (round == null) {
            return;
        }

        BigDecimal current =
                round.getCurrentMultiplier();

        BigDecimal crashPoint =
                round.getCrashPoint();

        BigDecimal next =
                current.add(new BigDecimal("0.00"));

        if (next.compareTo(crashPoint) >= 0) {

            gameRoundService.crashRound(
                    round.getId()
            );

            betService.markBetsAsLost(round);

            return;
        }

        round.setCurrentMultiplier(next);

        gameRoundRepository.save(round);
    }

    private void createNewRound() {

        gameRoundService.createRound();

        GameRound round =
                gameRoundRepository
                        .findFirstByStatusOrderByIdDesc(
                                GameStatus.WAITING
                        )
                        .orElseThrow();

        gameRoundService.startRound(
                round.getId()
        );
    }
}