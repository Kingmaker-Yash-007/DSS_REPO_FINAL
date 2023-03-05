package com.dss.project.dto;

public class ResortsSkiersDTO {

    private Integer numSkiers;
    private String responseMessage;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public ResortsSkiersDTO() {
    }

    public ResortsSkiersDTO(Integer numSkiers, String responseMessage) {
        this.numSkiers = numSkiers;
        this.responseMessage =  responseMessage;
    }

    public Integer getNumSkiers() {
        return numSkiers;
    }

    public void setNumSkiers(Integer numSkiers) {
        this.numSkiers = numSkiers;
    }

}
