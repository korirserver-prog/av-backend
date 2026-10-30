package com.game.aviator.wallet.dto;

import com.game.aviator.wallet.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        TransactionType type,
        BigDecimal amount,
        BigDecimal balanceAfter,
        LocalDateTime createdAt
) {
}