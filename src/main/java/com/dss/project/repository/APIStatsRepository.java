package com.dss.project.repository;

import com.dss.project.model.APIStats;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface APIStatsRepository extends MongoRepository<APIStats, String> {

    @Query("{ 'URL' : ?0, 'operation' : ?1}")
    List<APIStats> findByURLAndOperation(String url, String operation, Sort sort);
}
