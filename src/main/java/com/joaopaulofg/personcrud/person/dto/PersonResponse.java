package com.joaopaulofg.personcrud.person.dto;

import com.joaopaulofg.personcrud.person.model.Address;
import com.joaopaulofg.personcrud.person.model.PhoneNumber;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PersonResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        String documentNumber,
        LocalDate dateOfBirth,
        List<Address> addresses,
        List<PhoneNumber> phoneNumbers,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
