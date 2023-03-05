package com.dss.project.dto;


public class ResortsListDTO {

    private Integer resortID;
    private String resortName;

    public ResortsListDTO(Integer resortID, String resortName) {
        this.resortID = resortID;
        this.resortName = resortName;
    }

    public ResortsListDTO() {
    }

    public Integer getResortID() {
        return resortID;
    }

    public void setResortID(Integer resortID) {
        this.resortID = resortID;
    }

    public String getResortName() {
        return resortName;
    }

    public void setResortName(String resortName) {
        this.resortName = resortName;
    }
}
