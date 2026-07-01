package com.joaopaulofg.personcrud.person.dto;

public record PersonFilterRequest(
        String name,
        String email,
        String documentNumber
) {
}
