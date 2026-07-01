package com.joaopaulofg.personcrud.person.service;

import com.joaopaulofg.personcrud.person.dto.AddressRequest;
import com.joaopaulofg.personcrud.person.dto.CreatePersonRequest;
import com.joaopaulofg.personcrud.person.dto.PersonFilterRequest;
import com.joaopaulofg.personcrud.person.dto.PersonResponse;
import com.joaopaulofg.personcrud.person.dto.PhoneNumberRequest;
import com.joaopaulofg.personcrud.person.dto.UpdatePersonRequest;
import com.joaopaulofg.personcrud.person.model.Address;
import com.joaopaulofg.personcrud.person.model.Person;
import com.joaopaulofg.personcrud.person.model.PhoneNumber;
import com.joaopaulofg.personcrud.person.repository.PersonRepository;
import com.joaopaulofg.personcrud.shared.exception.ResourceNotFoundException;
import com.joaopaulofg.personcrud.shared.pagination.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PersonService {

    private static final int MAX_PAGE_SIZE = 100;

    private final PersonRepository personRepository;
    private final PersonNormalizer personNormalizer;

    public PageResponse<PersonResponse> findAll(
            PersonFilterRequest filter,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        int normalizedPage = Math.max(page, 0);
        int normalizedSize = normalizePageSize(size);

        Sort sort = buildSort(sortBy, direction);
        PageRequest pageRequest = PageRequest.of(normalizedPage, normalizedSize, sort);

        var personsPage = personRepository.findAllByFilter(filter, pageRequest);

        return new PageResponse<>(
                personsPage.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList(),
                personsPage.getNumber(),
                personsPage.getSize(),
                personsPage.getTotalElements(),
                personsPage.getTotalPages(),
                personsPage.isFirst(),
                personsPage.isLast()
        );
    }

    public PersonResponse findById(String id) {
        Person person = findPersonById(id);
        return toResponse(person);
    }

    public PersonResponse create(CreatePersonRequest request) {
        String normalizedEmail = personNormalizer.normalizeEmail(request.email());
        String normalizedDocument = personNormalizer.normalizeDocumentNumber(request.documentNumber());

        validateUniqueEmail(normalizedEmail);
        validateUniqueDocumentNumber(normalizedDocument);

        Person person = Person.builder()
                .firstName(personNormalizer.normalizeName(request.firstName()))
                .lastName(personNormalizer.normalizeName(request.lastName()))
                .email(normalizedEmail)
                .documentNumber(normalizedDocument)
                .dateOfBirth(request.dateOfBirth())
                .addresses(toAddresses(request.addresses()))
                .phoneNumbers(toPhoneNumbers(request.phoneNumbers()))
                .build();

        Person savedPerson = personRepository.save(person);

        return toResponse(savedPerson);
    }

    public PersonResponse update(String id, UpdatePersonRequest request) {
        Person person = findPersonById(id);

        String normalizedEmail = personNormalizer.normalizeEmail(request.email());
        String normalizedDocument = personNormalizer.normalizeDocumentNumber(request.documentNumber());

        if (normalizedEmail != null && !Objects.equals(person.getEmail(), normalizedEmail)) {
            validateUniqueEmailForUpdate(normalizedEmail, id);
            person.setEmail(normalizedEmail);
        }

        if (normalizedDocument != null && !Objects.equals(person.getDocumentNumber(), normalizedDocument)) {
            validateUniqueDocumentNumberForUpdate(normalizedDocument, id);
            person.setDocumentNumber(normalizedDocument);
        }

        if (request.firstName() != null) {
            person.setFirstName(personNormalizer.normalizeName(request.firstName()));
        }

        if (request.lastName() != null) {
            person.setLastName(personNormalizer.normalizeName(request.lastName()));
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

    public PersonResponse replaceAddresses(String id, List<AddressRequest> addresses) {
        Person person = findPersonById(id);
        person.setAddresses(toAddresses(addresses));

        Person updatedPerson = personRepository.save(person);

        return toResponse(updatedPerson);
    }

    public PersonResponse replacePhoneNumbers(String id, List<PhoneNumberRequest> phoneNumbers) {
        Person person = findPersonById(id);
        person.setPhoneNumbers(toPhoneNumbers(phoneNumbers));

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
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com este CPF");
        }
    }

    private void validateUniqueEmailForUpdate(String email, String id) {
        if (personRepository.existsByEmailAndIdNot(email, id)) {
            throw new IllegalArgumentException("Já existe outra pessoa cadastrada com este e-mail");
        }
    }

    private void validateUniqueDocumentNumberForUpdate(String documentNumber, String id) {
        if (personRepository.existsByDocumentNumberAndIdNot(documentNumber, id)) {
            throw new IllegalArgumentException("Já existe outra pessoa cadastrada com este CPF");
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
                        .street(personNormalizer.normalizeName(request.street()))
                        .number(personNormalizer.normalizeName(request.number()))
                        .complement(personNormalizer.normalizeName(request.complement()))
                        .neighborhood(personNormalizer.normalizeName(request.neighborhood()))
                        .city(personNormalizer.normalizeName(request.city()))
                        .state(personNormalizer.normalizeState(request.state()))
                        .zipCode(personNormalizer.normalizeZipCode(request.zipCode()))
                        .build())
                .toList();
    }

    private List<PhoneNumber> toPhoneNumbers(List<PhoneNumberRequest> requests) {
        if (requests == null) {
            return List.of();
        }

        return requests.stream()
                .map(request -> PhoneNumber.builder()
                        .number(personNormalizer.normalizePhoneNumber(request.number()))
                        .type(personNormalizer.normalizeName(request.type()))
                        .build())
                .toList();
    }

    private int normalizePageSize(int size) {
        if (size <= 0) {
            return 10;
        }

        return Math.min(size, MAX_PAGE_SIZE);
    }

    private Sort buildSort(String sortBy, String direction) {
        String safeSortBy = resolveSortBy(sortBy);

        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(sortDirection, safeSortBy);
    }

    private String resolveSortBy(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return "createdAt";
        }

        return switch (sortBy) {
            case "firstName", "lastName", "email", "documentNumber", "dateOfBirth", "createdAt", "updatedAt" -> sortBy;
            default -> "createdAt";
        };
    }
}