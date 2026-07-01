package com.joaopaulofg.personcrud.person.repository;

import com.joaopaulofg.personcrud.person.dto.PersonFilterRequest;
import com.joaopaulofg.personcrud.person.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PersonRepositoryImpl implements PersonRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Person> findAllByFilter(PersonFilterRequest filter, Pageable pageable) {
        Query query = new Query();

        List<Criteria> criteria = new ArrayList<>();

        if (hasText(filter.name())) {
            Pattern pattern = Pattern.compile(Pattern.quote(filter.name()), Pattern.CASE_INSENSITIVE);

            criteria.add(new Criteria().orOperator(
                    Criteria.where("firstName").regex(pattern),
                    Criteria.where("lastName").regex(pattern)
            ));
        }

        if (hasText(filter.email())) {
            Pattern pattern = Pattern.compile(Pattern.quote(filter.email()), Pattern.CASE_INSENSITIVE);
            criteria.add(Criteria.where("email").regex(pattern));
        }

        if (hasText(filter.documentNumber())) {
            String normalizedDocument = filter.documentNumber().replaceAll("\\D", "");
            criteria.add(Criteria.where("documentNumber").is(normalizedDocument));
        }

        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria));
        }

        long total = mongoTemplate.count(query, Person.class);

        query.with(pageable);

        List<Person> persons = mongoTemplate.find(query, Person.class);

        return new PageImpl<>(persons, pageable, total);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
