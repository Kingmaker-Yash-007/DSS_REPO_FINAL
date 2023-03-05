package com.dss.project.dto;

public class LiftRideEventDTO {

    private Integer skierId;
    private Integer resortId;
    private Integer liftId;
    private Integer seasonId;
    private Integer dayId;
    private Integer time;

    public LiftRideEventDTO() {
    }

    public LiftRideEventDTO(Integer skierId, Integer resortId, Integer liftId,
                            Integer seasonId, Integer dayId, Integer time) {
        this.skierId = skierId;
        this.resortId = resortId;
        this.liftId = liftId;
        this.seasonId = seasonId;
        this.dayId = dayId;
        this.time = time;
    }

    public Integer getSkierId() {
        return skierId;
    }

    public void setSkierId(Integer skierId) {
        this.skierId = skierId;
    }

    public Integer getResortId() {
        return resortId;
    }

    public void setResortId(Integer resortId) {
        this.resortId = resortId;
    }

    public Integer getLiftId() {
        return liftId;
    }

    public void setLiftId(Integer liftId) {
        this.liftId = liftId;
    }

    public Integer getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Integer seasonId) {
        this.seasonId = seasonId;
    }

    public Integer getDayId() {
        return dayId;
    }

    public void setDayId(Integer dayId) {
        this.dayId = dayId;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
