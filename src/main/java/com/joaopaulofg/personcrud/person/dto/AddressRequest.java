package com.joaopaulofg.personcrud.person.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRequest(

        @NotBlank(message = "Rua é obrigatória")
        @Size(max = 120, message = "Rua deve ter no máximo 120 caracteres")
        String street,

        @NotBlank(message = "Número é obrigatório")
        @Size(max = 20, message = "Número deve ter no máximo 20 caracteres")
        String number,

        @Size(max = 120, message = "Complemento deve ter no máximo 120 caracteres")
        String complement,

        @NotBlank(message = "Bairro é obrigatório")
        @Size(max = 80, message = "Bairro deve ter no máximo 80 caracteres")
        String neighborhood,

        @NotBlank(message = "Cidade é obrigatória")
        @Size(max = 80, message = "Cidade deve ter no máximo 80 caracteres")
        String city,

        @NotBlank(message = "Estado é obrigatório")
        @Size(min = 2, max = 2, message = "Estado deve conter 2 caracteres")
        String state,

        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido")
        String zipCode
) {
}
