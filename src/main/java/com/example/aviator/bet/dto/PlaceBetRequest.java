package com.example.aviator.bet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PlaceBetRequest(

        @NotNull(message = "Bet amount is required")
        @DecimalMin(
                value = "1.00",
                message = "Minimum bet is 1.00"
        )
        BigDecimal amount

) {
}