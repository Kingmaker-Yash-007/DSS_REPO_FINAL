package com.dss.project.model;

import com.dss.project.dto.APIStatsDTO;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "APIStats")
public class APIStats {

    @Id
    private ObjectId objectId;
    @Field
    private String URL;
    @Field
    private String operation;
    @Field
    private long mean;
    @Field
    private long max;

    public APIStats() {
    }

    public APIStats(String URL, String operation, long mean, long max) {
        this.URL = URL;
        this.operation = operation;
        this.mean = mean;
        this.max = max;
    }

    public APIStats(APIStatsDTO apiStatsDTO) {
        this.URL = apiStatsDTO.getURL();
        this.operation = apiStatsDTO.getOperation();
        this.mean = apiStatsDTO.getMean();
        this.max = apiStatsDTO.getMax();
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public long getMean() {
        return mean;
    }

    public void setMean(long mean) {
        this.mean = mean;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "APIStats{" +
                "URL='" + URL + '\'' +
                ", operation='" + operation + '\'' +
                ", mean=" + mean +
                ", max=" + max +
                '}';
    }
}
