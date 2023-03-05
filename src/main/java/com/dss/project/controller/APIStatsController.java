package com.dss.project.controller;

import com.dss.project.dto.APIStatsDTO;
import com.dss.project.model.APIStats;
import com.dss.project.repository.APIStatsRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
@Tag(name = "Statistics")
public class APIStatsController {

    @Autowired
    private APIStatsRepository apiStatsRepository;

    @GetMapping
    @Operation(summary = "Get API performance statistics")
    public ResponseEntity<Map<String, List<APIStatsDTO>>> getApiStatistics() {

        // Retrieve API performance statistics for the /resorts endpoint
        APIStats apiStats = apiStatsRepository.findByURLAndOperation("/resorts", "GET", Sort.by(Sort.Direction.DESC, "mean")).get(0);

        // Create response
        Map<String, List<APIStatsDTO>> response = new HashMap<>();

        // Create list of APIStatsDTO objects
        List<APIStatsDTO> apiStatsList = new ArrayList<>();

        // Create APIStatsDTO object and add to list
        APIStatsDTO apiStatsDTO = new APIStatsDTO();
        apiStatsDTO.setURL("/resorts");
        apiStatsDTO.setOperation("GET");
        apiStatsDTO.setMean(apiStats.getMean());
        apiStatsDTO.setMax(apiStats.getMax());
        apiStatsList.add(apiStatsDTO);

        // Add APIStatsDTO list to response
        response.put("endpointStats", apiStatsList);

        return ResponseEntity.ok(response);
    }


}
