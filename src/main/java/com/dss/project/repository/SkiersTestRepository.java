package com.dss.project.repository;

import com.dss.project.model.SkiersTestData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkiersTestRepository extends MongoRepository<SkiersTestData, String> {
}
