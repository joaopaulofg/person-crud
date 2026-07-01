package com.joaopaulofg.personcrud.person.service;

import com.joaopaulofg.personcrud.person.dto.AddressRequest;
import com.joaopaulofg.personcrud.person.dto.CreatePersonRequest;
import com.joaopaulofg.personcrud.person.dto.PersonResponse;
import com.joaopaulofg.personcrud.person.dto.PhoneNumberRequest;
import com.joaopaulofg.personcrud.person.dto.UpdatePersonRequest;
import com.joaopaulofg.personcrud.person.model.Address;
import com.joaopaulofg.personcrud.person.model.Person;
import com.joaopaulofg.personcrud.person.model.PhoneNumber;
import com.joaopaulofg.personcrud.person.repository.PersonRepository;
import com.joaopaulofg.personcrud.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public List<PersonResponse> findAll() {
        return personRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PersonResponse findById(String id) {
        Person person = findPersonById(id);
        return toResponse(person);
    }

    public PersonResponse create(CreatePersonRequest request) {
        validateUniqueEmail(request.email());
        validateUniqueDocumentNumber(request.documentNumber());

        Person person = Person.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .documentNumber(request.documentNumber())
                .dateOfBirth(request.dateOfBirth())
                .addresses(toAddresses(request.addresses()))
                .phoneNumbers(toPhoneNumbers(request.phoneNumbers()))
                .build();

        Person savedPerson = personRepository.save(person);

        return toResponse(savedPerson);
    }

    public PersonResponse update(String id, UpdatePersonRequest request) {
        Person person = findPersonById(id);

        if (request.email() != null && !Objects.equals(person.getEmail(), request.email())) {
            validateUniqueEmail(request.email());
            person.setEmail(request.email());
        }

        if (request.documentNumber() != null && !Objects.equals(person.getDocumentNumber(), request.documentNumber())) {
            validateUniqueDocumentNumber(request.documentNumber());
            person.setDocumentNumber(request.documentNumber());
        }

        if (request.firstName() != null) {
            person.setFirstName(request.firstName());
        }

        if (request.lastName() != null) {
            person.setLastName(request.lastName());
        }

        if (request.dateOfBirth() != null) {
            person.setDateOfBirth(request.dateOfBirth());
        }

        if (request.addresses() != null) {
            person.setAddresses(toAddresses(request.addresses()));
        }

        if (request.phoneNumbers() != null) {
            person.setPhoneNumbers(toPhoneNumbers(request.phoneNumbers()));
        }

        Person updatedPerson = personRepository.save(person);

        return toResponse(updatedPerson);
    }

    public void delete(String id) {
        Person person = findPersonById(id);
        personRepository.delete(person);
    }

    private Person findPersonById(String id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com ID: " + id));
    }

    private void validateUniqueEmail(String email) {
        if (personRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com este e-mail");
        }
    }

    private void validateUniqueDocumentNumber(String documentNumber) {
        if (personRepository.existsByDocumentNumber(documentNumber)) {
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com este documento");
        }
    }

    private PersonResponse toResponse(Person person) {
        return new PersonResponse(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getEmail(),
                person.getDocumentNumber(),
                person.getDateOfBirth(),
                person.getAddresses(),
                person.getPhoneNumbers(),
                person.getCreatedAt(),
                person.getUpdatedAt()
        );
    }

    private List<Address> toAddresses(List<AddressRequest> requests) {
        if (requests == null) {
            return List.of();
        }

        return requests.stream()
                .map(request -> Address.builder()
                        .street(request.street())
                        .number(request.number())
                        .complement(request.complement())
                        .neighborhood(request.neighborhood())
                        .city(request.city())
                        .state(request.state())
                        .zipCode(request.zipCode())
                        .build())
                .toList();
    }

    private List<PhoneNumber> toPhoneNumbers(List<PhoneNumberRequest> requests) {
        if (requests == null) {
            return List.of();
        }

        return requests.stream()
                .map(request -> PhoneNumber.builder()
                        .number(request.number())
                        .type(request.type())
                        .build())
                .toList();
    }
}