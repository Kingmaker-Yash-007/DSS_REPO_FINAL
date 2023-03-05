package com.dss.project.dto;

public class SkierVerticalDTO {

    private String seasonId;
    private Integer totalVert;

    public SkierVerticalDTO() {
    }

    public SkierVerticalDTO(String seasonId, Integer totalVert) {
        this.seasonId = seasonId;
        this.totalVert = totalVert;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public Integer getTotalVert() {
        return totalVert;
    }

    public void setTotalVert(Integer totalVert) {
        this.totalVert = totalVert;
    }
}
