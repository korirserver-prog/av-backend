package com.example.aviator.bet;

import com.example.aviator.user.User;
import com.example.aviator.game.GameRound;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bets")
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "round_id", nullable = false)
    private GameRound round;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(precision = 10, scale = 2)
    private BigDecimal cashoutMultiplier;

    @Column(precision = 19, scale = 2)
    private BigDecimal payout;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BetStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime cashedOutAt;

    public Bet() {
    }

    public Bet(
            User user,
            GameRound round,
            BigDecimal amount
    ) {
        this.user = user;
        this.round = round;
        this.amount = amount;
        this.status = BetStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public GameRound getRound() {
        return round;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getCashoutMultiplier() {
        return cashoutMultiplier;
    }

    public BigDecimal getPayout() {
        return payout;
    }

    public BetStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCashedOutAt() {
        return cashedOutAt;
    }

    public void setCashoutMultiplier(BigDecimal cashoutMultiplier) {
        this.cashoutMultiplier = cashoutMultiplier;
    }

    public void setPayout(BigDecimal payout) {
        this.payout = payout;
    }

    public void setStatus(BetStatus status) {
        this.status = status;
    }

    public void setCashedOutAt(LocalDateTime cashedOutAt) {
        this.cashedOutAt = cashedOutAt;
    }
}