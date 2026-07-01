package com.joaopaulofg.personcrud.person.repository;

import com.joaopaulofg.personcrud.person.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PersonRepository extends MongoRepository<Person, String>, PersonRepositoryCustom {

    Optional<Person> findByEmail(String email);

    Optional<Person> findByDocumentNumber(String documentNumber);

    boolean existsByEmail(String email);

    boolean existsByDocumentNumber(String documentNumber);

    boolean existsByEmailAndIdNot(String email, String id);

    boolean existsByDocumentNumberAndIdNot(String documentNumber, String id);
}
