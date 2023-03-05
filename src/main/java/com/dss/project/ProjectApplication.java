package com.dss.project;

import com.dss.project.model.Skiers;
import com.dss.project.repository.ResortRepository;
import jakarta.annotation.PostConstruct;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ProjectApplication {

    @Autowired
    private ResortRepository resortRepository;

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @PostConstruct
    public void init() {
        if (resortRepository.count() == 0) {
            List<Skiers> resorts = new ArrayList<>();
            resorts.add(new Skiers(new ObjectId(),1, 1, 1, 1, 1, 1000, 60, 50, Arrays.asList("2019","2025"), "Whistler Blackcomb"));
            resorts.add(new Skiers(new ObjectId(),2, 1, 2, 2, 2, 2000, 120, 100, Arrays.asList("2020"), "Banff Ski Resort"));
            resorts.add(new Skiers(new ObjectId(),3, 2, 3, 3, 3, 500, 30, 20, Arrays.asList("2021"), "Mont Tremblant Ski Resort"));
            resorts.add(new Skiers(new ObjectId(),4, 2, 4, 4, 4, 1000, 60, 50, Arrays.asList("2022"), "Lake Louise Ski Resort"));
            resortRepository.saveAll(resorts);
        }
    }

}

