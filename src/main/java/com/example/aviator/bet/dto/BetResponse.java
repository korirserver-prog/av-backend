package com.example.aviator.bet.dto;

import com.example.aviator.bet.BetStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BetResponse(
        Long id,
        Long roundId,
        BigDecimal amount,
        BigDecimal cashoutMultiplier,
        BigDecimal payout,
        BetStatus status,
        LocalDateTime createdAt,
        LocalDateTime cashedOutAt
) {
}