package com.joaopaulofg.personcrud.person.dto;

import com.joaopaulofg.personcrud.shared.validation.Cpf;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record UpdatePersonRequest(

        @Size(max = 80, message = "Nome deve ter no máximo 80 caracteres")
        String firstName,

        @Size(max = 120, message = "Sobrenome deve ter no máximo 120 caracteres")
        String lastName,

        @Email(message = "E-mail inválido")
        String email,

        @Cpf
        String documentNumber,

        @Past(message = "Data de nascimento deve estar no passado")
        LocalDate dateOfBirth,

        @Valid
        List<AddressRequest> addresses,

        @Valid
        List<PhoneNumberRequest> phoneNumbers
) {
}
