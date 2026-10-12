package com.example.aviator.wallet.dto;

import java.math.BigDecimal;

public record WalletResponse(
        Long userId,
        BigDecimal balance,
        String currency
) {
}