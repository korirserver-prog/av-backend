package com.game.aviator.game.dto;

import com.game.aviator.game.GameStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GameRoundResponse(
        Long id,
        GameStatus status,
        BigDecimal crashPoint,
        BigDecimal currentMultiplier,
        LocalDateTime startedAt,
        LocalDateTime crashedAt
) {
}