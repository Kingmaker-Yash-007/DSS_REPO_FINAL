package com.dss.project.clientPartTwo;

import com.dss.project.dto.LiftRideEventDTO;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProjectApplicationTests {

    private static final int NUM_THREADS = 32;
    private static final int NUM_REQUESTS_PER_THREAD = 1000;
    private static final int MAX_TOTAL_NUM_REQUESTS = 10000;
    private static final int MAX_NUM_RETRIES = 5;

    private static ExecutorService executorService;
    private static RestTemplate restTemplate;
    private static String liftRideEventEndpoint;

    @BeforeAll
    public static void setup() {
        executorService = Executors.newFixedThreadPool(NUM_THREADS);
        restTemplate = new RestTemplate();
        liftRideEventEndpoint = "http://155.248.230.90:" + 8080 + "/v1/skiers/liftRideEvent";
        //liftRideEventEndpoint = "http://localhost:" + 8080 + "/v1/skiers/liftRideEvent";

    }

    @AfterAll
    public static void teardown() throws InterruptedException {
        executorService.shutdown();
        if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }

    @Test
    public void testCreateLiftRideEvent() throws InterruptedException {
        Random rand = new Random();
        AtomicInteger numSuccessfulRequests = new AtomicInteger();
        AtomicInteger numFailedRequests = new AtomicInteger();
        long startTime = System.currentTimeMillis();

        List<Long> latencies = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(MAX_TOTAL_NUM_REQUESTS);

        // Add a counter to keep track of the number of requests currently in progress
        AtomicInteger numRequestsInProgress = new AtomicInteger(0);

        for (int i = 0; i < NUM_THREADS; i++) {
            executorService.execute(() -> {
                int numRequests = 0;
                while (numRequests < NUM_REQUESTS_PER_THREAD) {
                    LiftRideEventDTO liftRideEventDTO = new LiftRideEventDTO();
                    liftRideEventDTO.setSkierId(new Random().nextInt(100000) + 1);
                    liftRideEventDTO.setResortId(new Random().nextInt(10) + 1);
                    liftRideEventDTO.setLiftId(new Random().nextInt(40) + 1);
                    liftRideEventDTO.setSeasonId(2022);
                    liftRideEventDTO.setDayId(1);
                    liftRideEventDTO.setTime(new Random().nextInt(360) + 1);

                    Instant requestStartTime = Instant.now();
                    int numRetries = 0;
                    while (numRetries <= MAX_NUM_RETRIES) {
                        try {
                            // Increment the counter for the number of requests in progress
                            numRequestsInProgress.incrementAndGet();

                            ResponseEntity<Void> responseEntity = restTemplate.postForEntity(liftRideEventEndpoint, liftRideEventDTO, Void.class);
                            if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
                                numSuccessfulRequests.getAndIncrement();
                                Instant requestEndTime = Instant.now();
                                long latency = Duration.between(requestStartTime, requestEndTime).toMillis();
                                latencies.add(latency);
                                writeCsvRecord(requestStartTime, latency, responseEntity.getStatusCode().value());
                                break;
                            }
                        } catch (RuntimeException ex) {
                            numRetries++;
                            if (numRetries > MAX_NUM_RETRIES) {
                                numFailedRequests.getAndIncrement();
                                Instant requestEndTime = Instant.now();
                                long latency = Duration.between(requestStartTime, requestEndTime).toMillis();
                                latencies.add(latency);
                                writeCsvRecord(requestStartTime, latency, -1);
                                break;
                            }
                        } finally {
                            // Decrement the counter for the number of requests in progress
                            numRequestsInProgress.decrementAndGet();
                        }
                    }
                    numRequests++;
                    latch.countDown();

                    // Check if the maximum number of requests has been reached
                    if (numSuccessfulRequests.get() + numFailedRequests.get() + numRequestsInProgress.get() >= MAX_TOTAL_NUM_REQUESTS) {
                        break;
                    }
                }
            });
        }

        latch.await();
        executorService.shutdown();
        if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        int numTotalRequests = numSuccessfulRequests.get() + numFailedRequests.get();

        // Calculate throughput
        double throughput = (double) numTotalRequests / totalTime * 1000;

        /// Calculate response time metrics
        double meanResponseTime = latencies.stream().mapToLong(Long::longValue).average().orElse(0.0);
        double medianResponseTime = calculateMedian(latencies);

        // Calculate p99
        Collections.sort(latencies);
        int p99Index = (int) Math.ceil(0.99 * latencies.size()) - 1;
        long p99ResponseTime = latencies.get(p99Index);

        // Calculate min and max response times
        long minResponseTime = latencies.isEmpty() ? 0 : latencies.get(0);
        long maxResponseTime = latencies.isEmpty() ? 0 : latencies.get(latencies.size() - 1);

        // Print out metrics
        System.out.println("Number of successful requests sent: " + numSuccessfulRequests);
        System.out.println("Number of unsuccessful requests: " + numFailedRequests);
        System.out.println("Start time: " + startTime + " ms");
        System.out.println("End Time: " + endTime + " ms");
        System.out.println("Total run time: " + totalTime + " ms");
        System.out.println("Total throughput: " + throughput + " requests per second");
        System.out.println("Mean response time: " + meanResponseTime + " ms");
        System.out.println("Median response time: " + medianResponseTime + " ms");
        System.out.println("99th percentile response time: " + p99ResponseTime + " ms");
        System.out.println("Min response time: " + minResponseTime + " ms");
        System.out.println("Max response time: " + maxResponseTime + " ms");

        MongoDatabase database = MongoClients.create().getDatabase("skirideapi");
        MongoCollection<Document> collection = database.getCollection("SkiersTest");
        collection.deleteMany(new Document());
    }
    // Write out CSV
    private void writeCsvRecord(Instant requestStartTime, long latency, int responseCode) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("latencies.csv", true))) {
            StringBuilder sb = new StringBuilder();
            sb.append(requestStartTime.toString()).append(",");
            sb.append("POST").append(",");
            sb.append(latency).append(",");
            sb.append(responseCode).append("\n");
            writer.write(sb.toString());
        } catch (IOException e) {
            System.err.println("Error writing record to CSV file: " + e.getMessage());
        }
    }

    private double calculateMedian(List<Long> latencies) {
        Collections.sort(latencies);
        int size = latencies.size();
        if (size % 2 == 0) {
            return (latencies.get(size / 2) + latencies.get((size / 2) - 1)) / 2.0;
        } else {
            return latencies.get(size / 2);
        }
    }
}
