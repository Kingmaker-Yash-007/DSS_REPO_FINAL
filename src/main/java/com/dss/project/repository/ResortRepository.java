package com.dss.project.repository;

import com.dss.project.model.Skiers;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ResortRepository extends MongoRepository<Skiers, String> {

    Optional<Skiers> findByResortIDAndSeasonIdAndDayId(Integer resortId, Integer seasonId, Integer dayId);

    List<Skiers> findByResortID(Integer resortID);

    Optional<Skiers> findByResortIDAndSeasonIdAndDayIdAndSkierId(Integer resortID, Integer seasonID, Integer dayID, Integer skierID);

    List<Skiers> findBySkierIdAndResortIDIn(Integer skierID, List<String> resortIds);

    List<Skiers> findBySkierId(Integer skierID);
}

