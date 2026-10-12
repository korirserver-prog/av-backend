//package com.example.aviator.game;
//
//import com.example.aviator.game.dto.GameRoundResponse;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Service
//public class GameRoundService {
//
//    private final GameRoundRepository gameRoundRepository;
//    private final CrashPointGenerator crashPointGenerator;
//
//    public GameRoundService(
//            GameRoundRepository gameRoundRepository,
//            CrashPointGenerator crashPointGenerator
//    ) {
//        this.gameRoundRepository = gameRoundRepository;
//        this.crashPointGenerator = crashPointGenerator;
//    }
//
//    public GameRoundResponse createRound() {
//
//        GameRound round = new GameRound(
//                GameStatus.WAITING,
//                crashPointGenerator.generate()
//        );
//
//        GameRound savedRound =
//                gameRoundRepository.save(round);
//
//        return toResponse(savedRound);
//    }
//
//    public GameRoundResponse startRound(Long id) {
//
//        GameRound round = gameRoundRepository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Round not found"));
//
//        round.setStatus(GameStatus.RUNNING);
//        round.setCurrentMultiplier(BigDecimal.ONE);
//
//        return toResponse(
//                gameRoundRepository.save(round)
//        );
//    }
//
//    public GameRoundResponse crashRound(Long id) {
//
//        GameRound round = gameRoundRepository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Round not found"));
//
//        round.setStatus(GameStatus.CRASHED);
//        round.setCrashedAt(LocalDateTime.now());
//
//        return toResponse(
//                gameRoundRepository.save(round)
//        );
//    }
//
//    public GameRoundResponse getRound(Long id) {
//
//        GameRound round = gameRoundRepository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Round not found"));
//
//        return toResponse(round);
//    }
//
//    public GameRoundResponse updateMultiplier(
//            Long id,
//            BigDecimal multiplier
//    ) {
//
//        GameRound round = gameRoundRepository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Round not found"));
//
//        if (round.getStatus() != GameStatus.RUNNING) {
//            throw new RuntimeException(
//                    "Round is not running"
//            );
//        }
//
//        if (multiplier.compareTo(round.getCrashPoint()) >= 0) {
//
//            round.setCurrentMultiplier(round.getCrashPoint());
//            round.setStatus(GameStatus.CRASHED);
//            round.setCrashedAt(LocalDateTime.now());
//
//        } else {
//
//            round.setCurrentMultiplier(multiplier);
//        }
//
//        return toResponse(
//                gameRoundRepository.save(round)
//        );
//    }
//
//    /**
//     * Automatically manages the game.
//     *
//     * Every second:
//     * - Creates a round if none exists
//     * - Starts WAITING rounds
//     * - Increases the multiplier
//     * - Crashes the round at the predetermined crash point
//     */
//    @Scheduled(fixedRate = 1000)
//    public void gameLoop() {
//
//        GameRound round =
//                gameRoundRepository
//                        .findFirstByOrderByIdDesc()
//                        .orElse(null);
//
//        // No round exists
//        if (round == null) {
//
//            createRound();
//
//            return;
//        }
//
//        // Start waiting round
//        if (round.getStatus() == GameStatus.WAITING) {
//
//            if (LocalDateTime.now().isBefore(
//                    round.getBettingEndsAt()
//            )) {
//
//                return;
//            }
//
//            startRound(round.getId());
//
//            System.out.println(
//                    "BETTING CLOSED: Round " +
//                            round.getId()
//            );
//
//            System.out.println(
//                    "GAME STARTED: Round " +
//                            round.getId()
//            );
//
//            return;
//        }
//        // Nothing to do if already crashed
//        if (round.getStatus() == GameStatus.CRASHED) {
//
//            createRound();
//
//            return;
//        }
//
//        // Increase multiplier
//        if (round.getStatus() == GameStatus.RUNNING) {
//
//            BigDecimal current =
//                    round.getCurrentMultiplier();
//
//            BigDecimal next =
//                    current.add(new BigDecimal("0.10"));
//
//            if (next.compareTo(round.getCrashPoint()) >= 0) {
//
//                round.setCurrentMultiplier(
//                        round.getCrashPoint()
//                );
//
//                round.setStatus(GameStatus.CRASHED);
//                round.setCrashedAt(LocalDateTime.now());
//
//                gameRoundRepository.save(round);
//
//                System.out.println(
//                        "GAME CRASHED: Round " +
//                                round.getId() +
//                                " at " +
//                                round.getCrashPoint() +
//                                "x"
//                );
//
//            } else {
//
//                round.setCurrentMultiplier(next);
//
//                gameRoundRepository.save(round);
//
//                System.out.println(
//                        "GAME ROUND " +
//                                round.getId() +
//                                " MULTIPLIER: " +
//                                next +
//                                "x"
//                );
//            }
//        }
//    }
//
//    public GameRoundResponse getCurrentRound() {
//
//        GameRound round = gameRoundRepository
//                .findFirstByStatusOrderByIdDesc(
//                        GameStatus.RUNNING
//                )
//                .orElseThrow(() ->
//                        new RuntimeException(
//                                "No running game round"
//                        ));
//
//        return toResponse(round);
//    }
//
//    private GameRoundResponse toResponse(GameRound round) {
//
//        return new GameRoundResponse(
//                round.getId(),
//                round.getStatus(),
//                round.getCrashPoint(),
//                round.getCurrentMultiplier(),
//                round.getStartedAt(),
//                round.getCrashedAt()
//        );
//    }
//}

package com.example.aviator.game;

import com.example.aviator.bet.BetService;
import com.example.aviator.game.dto.GameRoundResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class GameRoundService {

    private final GameRoundRepository gameRoundRepository;
    private final CrashPointGenerator crashPointGenerator;
    private final BetService betService;

    public GameRoundService(
            GameRoundRepository gameRoundRepository,
            CrashPointGenerator crashPointGenerator,
            BetService betService
    ) {
        this.gameRoundRepository = gameRoundRepository;
        this.crashPointGenerator = crashPointGenerator;
        this.betService = betService;
    }

    public GameRoundResponse createRound() {

        GameRound round = new GameRound(
                GameStatus.WAITING,
                crashPointGenerator.generate()
        );

        GameRound savedRound =
                gameRoundRepository.save(round);

        System.out.println(
                "BETTING OPEN: Round " +
                        savedRound.getId() +
                        " | Crash point: " +
                        savedRound.getCrashPoint() +
                        "x"
        );

        return toResponse(savedRound);
    }

    public GameRoundResponse startRound(Long id) {

        GameRound round =
                gameRoundRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Round not found"
                                ));

        round.setStatus(GameStatus.RUNNING);
        round.setCurrentMultiplier(
                BigDecimal.ONE
        );

        GameRound savedRound =
                gameRoundRepository.save(round);

        System.out.println(
                "BETTING CLOSED: Round " +
                        savedRound.getId()
        );

        System.out.println(
                "GAME STARTED: Round " +
                        savedRound.getId() +
                        " at 1.00x"
        );

        return toResponse(savedRound);
    }

    public GameRoundResponse crashRound(Long id) {

        GameRound round =
                gameRoundRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Round not found"
                                ));

        round.setCurrentMultiplier(
                round.getCrashPoint()
        );

        round.setStatus(
                GameStatus.CRASHED
        );

        round.setCrashedAt(
                LocalDateTime.now()
        );

        GameRound savedRound =
                gameRoundRepository.save(round);

        // All bets that were not cashed out lose.
        betService.markBetsAsLost(savedRound);

        System.out.println(
                "GAME CRASHED: Round " +
                        savedRound.getId() +
                        " at " +
                        savedRound.getCrashPoint() +
                        "x"
        );

        return toResponse(savedRound);
    }

    public GameRoundResponse getRound(Long id) {

        GameRound round =
                gameRoundRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Round not found"
                                ));

        return toResponse(round);
    }

    public GameRoundResponse updateMultiplier(
            Long id,
            BigDecimal multiplier
    ) {

        GameRound round =
                gameRoundRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Round not found"
                                ));

        if (round.getStatus() != GameStatus.RUNNING) {
            throw new RuntimeException(
                    "Round is not running"
            );
        }

        if (multiplier.compareTo(
                round.getCrashPoint()
        ) >= 0) {

            round.setCurrentMultiplier(
                    round.getCrashPoint()
            );

            round.setStatus(
                    GameStatus.CRASHED
            );

            round.setCrashedAt(
                    LocalDateTime.now()
            );

            GameRound savedRound =
                    gameRoundRepository.save(round);

            betService.markBetsAsLost(savedRound);

            return toResponse(savedRound);
        }

        round.setCurrentMultiplier(multiplier);

        return toResponse(
                gameRoundRepository.save(round)
        );
    }

    /**
     * Main game loop.
     *
     * WAITING:
     *     users have 5 seconds to place bets
     *
     * RUNNING:
     *     multiplier increases every second
     *
     * CRASHED:
     *     active bets are lost and a new
     *     betting round is created.
     */
    @Scheduled(fixedRate = 100)
    public void gameLoop() {

        GameRound round =
                gameRoundRepository
                        .findFirstByOrderByIdDesc()
                        .orElse(null);

        // No round exists.
        if (round == null) {

            createRound();

            return;
        }

        // ------------------------------------------------
        // WAITING / BETTING PHASE
        // ------------------------------------------------

        if (round.getStatus() == GameStatus.WAITING) {

            LocalDateTime bettingEnds =
                    round.getStartedAt()
                            .plusSeconds(10);

            if (LocalDateTime.now()
                    .isBefore(bettingEnds)) {

                return;
            }

            startRound(round.getId());

            return;
        }

        // ------------------------------------------------
        // CRASHED
        // ------------------------------------------------

        if (round.getStatus() == GameStatus.CRASHED) {

            createRound();

            return;
        }

        // ------------------------------------------------
        // RUNNING
        // ------------------------------------------------

        if (round.getStatus() == GameStatus.RUNNING) {

            BigDecimal current =
                    round.getCurrentMultiplier();

            BigDecimal next =
                    current.add(
                            new BigDecimal("0.01")
                    );

            // Crash at predetermined point.
            if (next.compareTo(
                    round.getCrashPoint()
            ) >= 0) {

                round.setCurrentMultiplier(
                        round.getCrashPoint()
                );

                round.setStatus(
                        GameStatus.CRASHED
                );

                round.setCrashedAt(
                        LocalDateTime.now()
                );

                GameRound savedRound =
                        gameRoundRepository.save(round);

                // Mark all remaining bets as LOST.
                betService.markBetsAsLost(
                        savedRound
                );

                System.out.println(
                        "GAME CRASHED: Round " +
                                savedRound.getId() +
                                " at " +
                                savedRound.getCrashPoint() +
                                "x"
                );

            } else {

                round.setCurrentMultiplier(next);

                gameRoundRepository.save(round);

//                System.out.println(
//                        "GAME ROUND " +
//                                round.getId() +
//                                " MULTIPLIER: " +
//                                next +
//                                "x"
//                );
            }
        }
    }

    public GameRoundResponse getCurrentRound() {

        GameRound round =
                gameRoundRepository
                        .findFirstByOrderByIdDesc()
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No game round available"
                                ));

        return toResponse(round);
    }

    private GameRoundResponse toResponse(
            GameRound round
    ) {

        return new GameRoundResponse(
                round.getId(),
                round.getStatus(),
                round.getCrashPoint(),
                round.getCurrentMultiplier(),
                round.getStartedAt(),
                round.getCrashedAt()
        );
    }
}