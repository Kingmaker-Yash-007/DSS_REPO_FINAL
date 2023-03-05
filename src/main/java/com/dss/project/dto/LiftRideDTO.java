package com.dss.project.dto;

public class LiftRideDTO {

    private Integer time;
    private Integer liftId;

    public LiftRideDTO() {
    }

    public LiftRideDTO(Integer time, Integer liftId) {
        this.time = time;
        this.liftId = liftId;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getLiftId() {
        return liftId;
    }

    public void setLiftId(Integer liftId) {
        this.liftId = liftId;
    }
}
