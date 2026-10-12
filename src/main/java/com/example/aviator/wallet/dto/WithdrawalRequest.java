package com.example.aviator.wallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WithdrawalRequest(

        @NotNull(message = "Amount is required")
        @DecimalMin(
                value = "1.00",
                message = "Minimum withdrawal is 1.00"
        )
        BigDecimal amount

) {
}