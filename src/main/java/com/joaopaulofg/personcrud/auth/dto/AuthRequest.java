package com.joaopaulofg.personcrud.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(

        @NotBlank(message = "API Key é obrigatória")
        String apiKey
) {
}