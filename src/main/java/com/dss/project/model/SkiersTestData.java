package com.dss.project.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "SkiersTest")
public class SkiersTestData {

    @Id
    private ObjectId objectId;
    @Field
    private Integer resortID;
    @Field
    private Integer seasonId;
    @Field
    private Integer skierId;
    @Field
    private Integer liftId;
    @Field
    private Integer dayId;
    @Field
    private Integer time;

    public SkiersTestData() {
    }

    public SkiersTestData(ObjectId objectId, Integer resortID,
                          Integer seasonId, Integer skierId, Integer liftId, Integer dayId, Integer time) {
        this.objectId = objectId;
        this.resortID = resortID;
        this.seasonId = seasonId;
        this.skierId = skierId;
        this.liftId = liftId;
        this.dayId = dayId;
        this.time = time;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public Integer getResortID() {
        return resortID;
    }

    public void setResortID(Integer resortID) {
        this.resortID = resortID;
    }

    public Integer getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Integer seasonId) {
        this.seasonId = seasonId;
    }

    public Integer getSkierId() {
        return skierId;
    }

    public void setSkierId(Integer skierId) {
        this.skierId = skierId;
    }

    public Integer getLiftId() {
        return liftId;
    }

    public void setLiftId(Integer liftId) {
        this.liftId = liftId;
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

    @Override
    public String toString() {
        return "SkiersTestData{" +
                "objectId=" + objectId +
                ", resortID=" + resortID +
                ", seasonId=" + seasonId +
                ", skierId=" + skierId +
                ", liftId=" + liftId +
                ", dayId=" + dayId +
                ", time=" + time +
                '}';
    }
}
