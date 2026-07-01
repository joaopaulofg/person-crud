package com.joaopaulofg.personcrud.person.controller;

import com.joaopaulofg.personcrud.person.dto.AddressRequest;
import com.joaopaulofg.personcrud.person.dto.CreatePersonRequest;
import com.joaopaulofg.personcrud.person.dto.PersonFilterRequest;
import com.joaopaulofg.personcrud.person.dto.PersonResponse;
import com.joaopaulofg.personcrud.person.dto.PhoneNumberRequest;
import com.joaopaulofg.personcrud.person.dto.UpdatePersonRequest;
import com.joaopaulofg.personcrud.person.service.PersonService;
import com.joaopaulofg.personcrud.shared.pagination.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public PageResponse<PersonResponse> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String documentNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        var filter = new PersonFilterRequest(name, email, documentNumber);

        return personService.findAll(
                filter,
                page,
                size,
                sortBy,
                direction
        );
    }

    @GetMapping("/{id}")
    public PersonResponse findById(@PathVariable String id) {
        return personService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonResponse create(@RequestBody @Valid CreatePersonRequest request) {
        return personService.create(request);
    }

    @PutMapping("/{id}")
    public PersonResponse update(
            @PathVariable String id,
            @RequestBody @Valid UpdatePersonRequest request
    ) {
        return personService.update(id, request);
    }

    @PatchMapping("/{id}")
    public PersonResponse patch(
            @PathVariable String id,
            @RequestBody @Valid UpdatePersonRequest request
    ) {
        return personService.update(id, request);
    }

    @PutMapping("/{id}/addresses")
    public PersonResponse replaceAddresses(
            @PathVariable String id,
            @RequestBody @Valid List<AddressRequest> addresses
    ) {
        return personService.replaceAddresses(id, addresses);
    }

    @PutMapping("/{id}/phone-numbers")
    public PersonResponse replacePhoneNumbers(
            @PathVariable String id,
            @RequestBody @Valid List<PhoneNumberRequest> phoneNumbers
    ) {
        return personService.replacePhoneNumbers(id, phoneNumbers);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        personService.delete(id);
    }
}