package com.joaopaulofg.personcrud.auth.dto;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}