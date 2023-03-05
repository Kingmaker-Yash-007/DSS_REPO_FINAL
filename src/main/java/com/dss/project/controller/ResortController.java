package com.dss.project.controller;

import com.dss.project.dto.*;
import com.dss.project.model.APIStats;
import com.dss.project.model.Skiers;
import com.dss.project.repository.APIStatsRepository;
import com.dss.project.repository.ResortRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/resorts")
@Tag(name = "Resorts")
public class ResortController {

    @Autowired
    private ResortRepository resortRepository;

    @Autowired
    private APIStatsRepository apiStatsRepository;

    @GetMapping
    @Operation(summary = "Get all resorts")
    public ResponseEntity<List<ResortsListDTO>> getAllResorts() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Skiers> resorts = resortRepository.findAll();
        List<ResortsListDTO> resortsDTO = new ArrayList<>();

        for (Skiers resort : resorts) {
            ResortsListDTO resortDTO = new ResortsListDTO();
            resortDTO.setResortID(resort.getResortID());
            resortDTO.setResortName(resort.getResortName());
            resortsDTO.add(resortDTO);
        }

        stopWatch.stop();
        long executionTime = stopWatch.getLastTaskTimeMillis(); // Execution time in milliseconds

        // Save execution time to a list
        List<Long> executionTimes = new ArrayList<>();
        executionTimes.add(executionTime);

        // Calculate mean and max values
        long mean = (long) executionTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
        long max = executionTimes.stream().mapToLong(Long::longValue).max().orElse(0);

        // Create response
        Map<String, List<ResortsListDTO>> response = new HashMap<>();
        response.put("resorts", resortsDTO);

        // Create APIStatsDTO object
        APIStatsDTO apiStatsDTO = new APIStatsDTO();
        apiStatsDTO.setURL("/resorts");
        apiStatsDTO.setOperation("GET");
        apiStatsDTO.setMean(mean);
        apiStatsDTO.setMax(max);

        // Save APIStatsDTO object to database
        APIStats apiStats = new APIStats(apiStatsDTO);
        apiStatsRepository.save(apiStats);

        return ResponseEntity.ok(response.get("resorts"));
    }

    @GetMapping("/{resortID}/seasons/{seasonID}/day/{dayID}/skiers")
    @Operation(summary = "Get unique skiers at a particular resort, season, and day")
    public ResponseEntity<ResortsSkiersDTO> getNumSkiers(@PathVariable Integer resortID,
                                                         @PathVariable Integer seasonID,
                                                         @PathVariable Integer dayID) {

        if (resortID <= 0 || seasonID <= 0 || dayID <= 0) {
            return ResponseEntity.badRequest().body(new ResortsSkiersDTO(null, "Invalid input: resortID, seasonID, and dayID must be greater than 0"));
        }

        Optional<Skiers> skiersOptional = resortRepository
                .findByResortIDAndSeasonIdAndDayId(resortID, seasonID, dayID);

        if (skiersOptional.isEmpty()) {
            ResortsSkiersDTO responseDto = new ResortsSkiersDTO();
            responseDto.setResponseMessage("Id Not Found");
            responseDto.setNumSkiers(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }

        Skiers skiers = skiersOptional.get();
        Integer numSkiers = skiers.getNumSkiers();
        ResortsSkiersDTO responseDTO = new ResortsSkiersDTO(numSkiers, "Success");
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{resortID}/seasons")
    @Operation(summary = "Get a list of seasons for a particular resort")
    public ResponseEntity<?> getSeasonsByResort(@PathVariable Integer resortID) {
        if (resortID <= 0) {
            ResponseMessageDTO responseDto = new ResponseMessageDTO();
            responseDto.setMessage("Invalid input: resortID must be greater than 0");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        List<Skiers> resorts = resortRepository.findByResortID(resortID);

        if (resorts.isEmpty()) {
            ResponseMessageDTO responseDto = new ResponseMessageDTO();
            responseDto.setMessage("Resort Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }

        List<String> seasons = resorts.get(0).getSeasons();

        SeasonsListDTO seasonsDto = new SeasonsListDTO();
        seasonsDto.setSeasons(seasons);

        return ResponseEntity.ok(seasonsDto);
    }



    @PostMapping("/{resortID}/seasons")
    @Operation(summary = "Add a season to the list of seasons for a particular resort")
    public ResponseEntity<ResponseMessageDTO> addSeasonByResort(@PathVariable Integer resortID,
                                                                @RequestBody String season) {
        if (resortID <= 0) {
            ResponseMessageDTO responseDto = new ResponseMessageDTO();
            responseDto.setMessage("Invalid input: resortID must be greater than 0");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        List<Skiers> resorts = resortRepository.findByResortID(resortID);

        if (resorts.isEmpty()) {
            ResponseMessageDTO responseDto = new ResponseMessageDTO();
            responseDto.setMessage("Resort Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }

        Skiers resort = resorts.get(0);
        List<String> existingSeasons = resort.getSeasons();
        existingSeasons.add(season);
        resort.setSeasons(existingSeasons);
        resortRepository.save(resort);

        ResponseMessageDTO responseDto = new ResponseMessageDTO();
        responseDto.setMessage("Season added successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}
