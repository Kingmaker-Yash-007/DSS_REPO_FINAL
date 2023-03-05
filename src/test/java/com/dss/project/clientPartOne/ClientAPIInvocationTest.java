package com.dss.project.clientPartOne;

import com.dss.project.dto.LiftRideEventDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientAPIInvocationTest {

    private static RestTemplate restTemplate;
    private static String liftRideEventEndpoint;


    @BeforeAll
    public static void setup() {
        restTemplate = new RestTemplate();
        liftRideEventEndpoint = "http://155.248.230.90:" + 8080 + "/v1/skiers/liftRideEvent";
        //liftRideEventEndpoint = "http://localhost:" + 8080 + "/v1/skiers/liftRideEvent";

    }

    @Test
    public void testLatency() {
        Random rand = new Random();
        AtomicInteger numSuccessfulRequests = new AtomicInteger();
        AtomicInteger numFailedRequests = new AtomicInteger();
        List<Long> responseTimes = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            LiftRideEventDTO liftRideEventDTO = new LiftRideEventDTO();
            liftRideEventDTO.setSkierId(rand.nextInt(100000) + 1);
            liftRideEventDTO.setResortId(rand.nextInt(10) + 1);
            liftRideEventDTO.setLiftId(rand.nextInt(40) + 1);
            liftRideEventDTO.setSeasonId(2022);
            liftRideEventDTO.setDayId(1);
            liftRideEventDTO.setTime(rand.nextInt(360) + 1);

            long startTime = System.currentTimeMillis();

            try {
                ResponseEntity<Void> responseEntity = restTemplate.postForEntity(liftRideEventEndpoint, liftRideEventDTO, Void.class);
                if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
                    numSuccessfulRequests.getAndIncrement();
                }
            } catch (RuntimeException ex) {
                numFailedRequests.getAndIncrement();
            }

            long endTime = System.currentTimeMillis();
            responseTimes.add(endTime - startTime);
        }

        double percentile = calculatePercentile(responseTimes, 5);
        double throughput = 1000.0 * 500.0 / percentile;

        System.out.println("Number of successful requests sent: " + numSuccessfulRequests);
        System.out.println("Number of unsuccessful requests: " + numFailedRequests);
        System.out.println("Throughput for the 5th percentile response time: " + throughput + " requests per second");
    }

    private double calculatePercentile(List<Long> responseTimes, int percentile) {
        Collections.sort(responseTimes);
        int index = (int) Math.ceil(percentile / 100.0 * responseTimes.size()) - 1;
        return responseTimes.get(index);
    }

}
