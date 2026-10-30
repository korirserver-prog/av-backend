package com.game.aviator.game;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "game_rounds")
public class GameRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal crashPoint;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    private LocalDateTime crashedAt;

    private LocalDateTime bettingEndsAt;





    @Column(precision = 10, scale = 2)
    private BigDecimal currentMultiplier;

    public GameRound() {
    }


public GameRound(
        GameStatus status,
        BigDecimal crashPoint
) {
    this.status = status;
    this.crashPoint = crashPoint;
    this.currentMultiplier = BigDecimal.ONE;
    this.startedAt = LocalDateTime.now();
}

    public Long getId() {
        return id;
    }

    public GameStatus getStatus() {
        return status;
    }

    public BigDecimal getCrashPoint() {
        return crashPoint;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getCrashedAt() {
        return crashedAt;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public void setCrashedAt(LocalDateTime crashedAt) {
        this.crashedAt = crashedAt;
    }

    public void setCurrentMultiplier(BigDecimal currentMultiplier) {
        this.currentMultiplier = currentMultiplier;
    }

    public BigDecimal getCurrentMultiplier() {
        return currentMultiplier;
    }

    public LocalDateTime getBettingEndsAt() {
        return bettingEndsAt;
    }

    public void setBettingEndsAt(LocalDateTime bettingEndsAt) {
        this.bettingEndsAt = bettingEndsAt;
    }





}