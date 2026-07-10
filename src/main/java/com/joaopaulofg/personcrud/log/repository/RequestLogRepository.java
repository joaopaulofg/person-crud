package com.joaopaulofg.personcrud.log.repository;

import com.joaopaulofg.personcrud.log.model.RequestLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RequestLogRepository extends MongoRepository<RequestLog, String> {
}