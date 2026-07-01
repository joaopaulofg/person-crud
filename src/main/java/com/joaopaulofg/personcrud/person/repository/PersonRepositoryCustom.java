package com.joaopaulofg.personcrud.person.repository;

import com.joaopaulofg.personcrud.person.dto.PersonFilterRequest;
import com.joaopaulofg.personcrud.person.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonRepositoryCustom {

    Page<Person> findAllByFilter(PersonFilterRequest filter, Pageable pageable);
}
