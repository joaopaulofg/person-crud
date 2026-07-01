package com.joaopaulofg.personcrud.person.dto;

import com.joaopaulofg.personcrud.shared.validation.Cpf;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CreatePersonRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 80, message = "Nome deve ter no máximo 80 caracteres")
        String firstName,

        @NotBlank(message = "Sobrenome é obrigatório")
        @Size(max = 120, message = "Sobrenome deve ter no máximo 120 caracteres")
        String lastName,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "CPF é obrigatório")
        @Cpf
        String documentNumber,

        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve estar no passado")
        LocalDate dateOfBirth,

        @Valid
        List<AddressRequest> addresses,

        @Valid
        List<PhoneNumberRequest> phoneNumbers
) {
}