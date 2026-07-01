package com.joaopaulofg.personcrud.person.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PhoneNumberRequest(

        @NotBlank(message = "Número de telefone é obrigatório")
        @Pattern(
                regexp = "\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}",
                message = "Telefone inválido"
        )
        String number,

        @NotBlank(message = "Tipo do telefone é obrigatório")
        String type
) {
}