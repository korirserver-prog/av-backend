package com.example.aviator.bet;

import com.example.aviator.user.User;
import com.example.aviator.user.UserRepository;
import com.example.aviator.game.GameRound;
import com.example.aviator.game.GameRoundRepository;
import com.example.aviator.game.GameStatus;
import com.example.aviator.wallet.Transaction;
import com.example.aviator.wallet.TransactionRepository;
import com.example.aviator.wallet.TransactionType;
import com.example.aviator.wallet.Wallet;
import com.example.aviator.wallet.WalletRepository;
import com.example.aviator.bet.dto.BetResponse;
import com.example.aviator.bet.dto.PlaceBetRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BetService {

    private final BetRepository betRepository;
    private final UserRepository userRepository;
    private final GameRoundRepository gameRoundRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public BetService(
            BetRepository betRepository,
            UserRepository userRepository,
            GameRoundRepository gameRoundRepository,
            WalletRepository walletRepository,
            TransactionRepository transactionRepository
    ) {
        this.betRepository = betRepository;
        this.userRepository = userRepository;
        this.gameRoundRepository = gameRoundRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public BetResponse placeBet(
            String username,
            PlaceBetRequest request
    ) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        /*
         * Bets are placed during the WAITING phase.
         * This gives users 5 seconds before the game starts.
         */
        GameRound round =
                gameRoundRepository
                        .findFirstByStatusOrderByIdDesc(
                                GameStatus.WAITING
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No betting round available"
                                ));

        /*
         * The betting window starts when the round is created.
         * Betting remains open for 5 seconds.
         */
        LocalDateTime bettingEnds =
                round.getStartedAt().plusSeconds(10);

        if (!LocalDateTime.now().isBefore(bettingEnds)) {

            throw new RuntimeException(
                    "Betting time has ended"
            );
        }

        Wallet wallet =
                walletRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Wallet not found"
                                ));

        if (request.amount()
                .compareTo(wallet.getBalance()) > 0) {

            throw new RuntimeException(
                    "Insufficient wallet balance"
            );
        }

        wallet.setBalance(
                wallet.getBalance()
                        .subtract(request.amount())
        );

        walletRepository.save(wallet);

        Bet bet = new Bet(
                user,
                round,
                request.amount()
        );

        Bet savedBet =
                betRepository.save(bet);

        Transaction transaction =
                new Transaction(
                        wallet,
                        TransactionType.BET,
                        request.amount().negate(),
                        wallet.getBalance()
                );

        transactionRepository.save(transaction);

        return toResponse(savedBet);
    }

    private BetResponse toResponse(Bet bet) {

        return new BetResponse(
                bet.getId(),
                bet.getRound().getId(),
                bet.getAmount(),
                bet.getCashoutMultiplier(),
                bet.getPayout(),
                bet.getStatus(),
                bet.getCreatedAt(),
                bet.getCashedOutAt()
        );
    }

    @Transactional
    public BetResponse cashOut(
            String username,
            Long betId
    ) {

        User user =
                userRepository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                ));

        Bet bet =
                betRepository.findById(betId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Bet not found"
                                ));

        if (!bet.getUser().getId().equals(user.getId())) {

            throw new RuntimeException(
                    "You cannot cash out this bet"
            );
        }

        if (bet.getStatus() != BetStatus.ACTIVE) {

            throw new RuntimeException(
                    "Bet is no longer active"
            );
        }

        GameRound round = bet.getRound();

        /*
         * Cashout is only allowed after the game has started.
         */
        if (round.getStatus() != GameStatus.RUNNING) {

            throw new RuntimeException(
                    "The round has not started or has already crashed"
            );
        }

        BigDecimal multiplier =
                round.getCurrentMultiplier();

        if (multiplier == null ||
                multiplier.compareTo(BigDecimal.ONE) < 0) {

            throw new RuntimeException(
                    "Invalid current multiplier"
            );
        }

        BigDecimal payout =
                bet.getAmount()
                        .multiply(multiplier)
                        .setScale(
                                2,
                                RoundingMode.DOWN
                        );

        Wallet wallet =
                walletRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Wallet not found"
                                ));

        wallet.setBalance(
                wallet.getBalance().add(payout)
        );

        walletRepository.save(wallet);

        bet.setCashoutMultiplier(multiplier);
        bet.setPayout(payout);
        bet.setStatus(BetStatus.CASHED_OUT);
        bet.setCashedOutAt(LocalDateTime.now());

        Bet savedBet =
                betRepository.save(bet);

        Transaction transaction =
                new Transaction(
                        wallet,
                        TransactionType.WIN,
                        payout,
                        wallet.getBalance()
                );

        transactionRepository.save(transaction);

        return toResponse(savedBet);
    }

    public List<BetResponse> getMyBets(
            String username
    ) {

        User user =
                userRepository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                ));

        return betRepository
                .findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void markBetsAsLost(
            GameRound round
    ) {

        List<Bet> activeBets =
                betRepository
                        .findByRound(round)
                        .stream()
                        .filter(bet ->
                                bet.getStatus()
                                        == BetStatus.ACTIVE
                        )
                        .toList();

        for (Bet bet : activeBets) {

            bet.setStatus(BetStatus.LOST);
        }
    }
}