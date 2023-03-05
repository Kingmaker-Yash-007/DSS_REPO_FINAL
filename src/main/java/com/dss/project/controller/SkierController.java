package com.dss.project.controller;

import com.dss.project.dto.LiftRideDTO;
import com.dss.project.dto.LiftRideEventDTO;
import com.dss.project.dto.SkierVerticalDTO;
import com.dss.project.model.Skiers;
import com.dss.project.model.SkiersTestData;
import com.dss.project.repository.ResortRepository;
import com.dss.project.repository.SkiersTestRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/skiers")
@Tag(name = "Skiers")
public class SkierController {

    @Autowired
    private ResortRepository resortRepository;

    @Autowired
    private SkiersTestRepository skiersTestRepository;

    @PostMapping("/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
    @Operation(summary = "write a new lift ride for the skier")
    public ResponseEntity<String> addLiftRide(
            @PathVariable Integer resortID,
            @PathVariable Integer seasonID,
            @PathVariable Integer dayID,
            @PathVariable Integer skierID,
            @RequestBody LiftRideDTO newLiftRideDTO) {

        // Check if the required fields in the request body are present
        if (newLiftRideDTO.getLiftId() == null || newLiftRideDTO.getTime() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lift ID and time are required fields.");
        }

        // Find the existing lift ride data for the skier
        Optional<Skiers> existingLiftRide = resortRepository.findByResortIDAndSeasonIdAndDayIdAndSkierId(resortID, seasonID, dayID, skierID);

        if (existingLiftRide.isPresent()) {
            // Update the existing lift ride data with the new values
            Skiers updatedLiftRide = existingLiftRide.get();
            updatedLiftRide.setLiftId(newLiftRideDTO.getLiftId());
            updatedLiftRide.setTime(newLiftRideDTO.getTime());

            // Save the updated lift ride data to the database
            resortRepository.save(updatedLiftRide);

            return ResponseEntity.ok("Lift ride updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No lift ride data found for the specified skier and date.");
        }
    }


    @GetMapping("/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
    @Operation(summary = "Get ski day vertical for a skier")
    public ResponseEntity<Integer> getTotalVertical(
            @PathVariable Integer resortID,
            @PathVariable Integer seasonID,
            @PathVariable Integer dayID,
            @PathVariable Integer skierID) {

        // Find the existing lift ride data for the skier on the specified day
        Optional<Skiers> existingLiftRide = resortRepository.findByResortIDAndSeasonIdAndDayIdAndSkierId(resortID, seasonID, dayID, skierID);

        if (existingLiftRide.isPresent()) {
            // Return the total vertical for the skier on the specified day
            Integer totalVertical = existingLiftRide.get().getTotalVert();
            return ResponseEntity.ok(totalVertical);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/skiers/{skierID}/vertical")
    @Operation(summary = "Get the total vertical for the skier for specified seasons at the specified resort")
    public List<SkierVerticalDTO> getSkierVertical(@PathVariable Integer skierID, @RequestBody Map<String, Object> request) {
        List<String> resortIds = (List<String>) request.get("resortIds");
        List<String> seasons = (List<String>) request.get("seasons");

        List<Skiers> skiersList;
        if (resortIds != null && !resortIds.isEmpty()) {
            skiersList = resortRepository.findBySkierIdAndResortIDIn(skierID, resortIds);
        } else {
            skiersList = resortRepository.findBySkierId(skierID);
        }

        List<SkierVerticalDTO> skierVerticalDTOList = new ArrayList<>();
        Map<String, Integer> seasonToVerticalMap = new HashMap<>();
        for (Skiers skiers : skiersList) {
            if (seasons == null || seasons.isEmpty() || seasons.contains(skiers.getSeasonId().toString())) {
                String season = skiers.getSeasonId().toString();
                Integer vertical = skiers.getTotalVert();
                if (seasonToVerticalMap.containsKey(season)) {
                    vertical += seasonToVerticalMap.get(season);
                }
                seasonToVerticalMap.put(season, vertical);
            }
        }

        for (Map.Entry<String, Integer> entry : seasonToVerticalMap.entrySet()) {
            SkierVerticalDTO skierVerticalDTO = new SkierVerticalDTO();
            skierVerticalDTO.setSeasonId(entry.getKey());
            skierVerticalDTO.setTotalVert(entry.getValue());
            skierVerticalDTOList.add(skierVerticalDTO);
        }

        return skierVerticalDTOList;
    }

    @PostMapping("/liftRideEvent")
    @Operation(summary = "Create a new lift ride event")
    public ResponseEntity<?> createLiftRideEvent(@RequestBody LiftRideEventDTO liftRideEventDTO) {
        // Check if the input values are within the valid range
        if (liftRideEventDTO.getSkierId() < 1 || liftRideEventDTO.getSkierId() > 100000 ||
                liftRideEventDTO.getResortId() < 1 || liftRideEventDTO.getResortId() > 10 ||
                liftRideEventDTO.getLiftId() < 1 || liftRideEventDTO.getLiftId() > 40 ||
                liftRideEventDTO.getSeasonId() != 2022 ||
                liftRideEventDTO.getDayId() != 1 ||
                liftRideEventDTO.getTime() < 1 || liftRideEventDTO.getTime() > 360) {
            return ResponseEntity.badRequest().body("Invalid input values");
        }

        // Create a new Skiers object
        SkiersTestData skiersTestData = new SkiersTestData();
        skiersTestData.setSkierId(liftRideEventDTO.getSkierId());
        skiersTestData.setResortID(liftRideEventDTO.getResortId());
        skiersTestData.setLiftId(liftRideEventDTO.getLiftId());
        skiersTestData.setSeasonId(liftRideEventDTO.getSeasonId());
        skiersTestData.setDayId(liftRideEventDTO.getDayId());
        skiersTestData.setTime(liftRideEventDTO.getTime());

        // Save the new Skiers object to the database
        skiersTestRepository.save(skiersTestData);

        return ResponseEntity.status(HttpStatus.CREATED).body("Lift ride event created successfully");
    }




}
