package com.dss.project.dto;

public class APIStatsDTO {
    private String URL;
    private String operation;
    private long mean;
    private long max;

    public APIStatsDTO(String URL, String operation, long mean, long max) {
        this.URL = URL;
        this.operation = operation;
        this.mean = mean;
        this.max = max;
    }

    public APIStatsDTO() {
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

}
