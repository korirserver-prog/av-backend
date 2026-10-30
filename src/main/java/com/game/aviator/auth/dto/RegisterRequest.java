package com.game.aviator.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Password is required")
        @Size(min = 4, max = 100, message = "Password must be at least 4 characters")
        String password
) {
}