package com.game.aviator.wallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositRequest(

        @NotNull(message = "Amount is required")
        @DecimalMin(
                value = "1.00",
                message = "Minimum deposit is 1.00"
        )
        BigDecimal amount

) {
}