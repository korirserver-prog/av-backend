package com.game.aviator.auth.dto;

public record AuthResponse(
        String token,
        Long userId,
        String username,
        String email,
        String role
) {
}