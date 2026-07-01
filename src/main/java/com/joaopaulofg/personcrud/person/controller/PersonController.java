package com.joaopaulofg.personcrud.person.controller;

import com.joaopaulofg.personcrud.person.dto.CreatePersonRequest;
import com.joaopaulofg.personcrud.person.dto.PersonResponse;
import com.joaopaulofg.personcrud.person.dto.UpdatePersonRequest;
import com.joaopaulofg.personcrud.person.service.PersonService;
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
    public List<PersonResponse> findAll() {
        return personService.findAll();
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        personService.delete(id);
    }
}