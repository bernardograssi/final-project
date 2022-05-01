package com.example.finalproject.model;

public class Response {
    private Double time;
    private Double magnitude;

    public Response(Double time, Double magnitude) {
        this.time = time;
        this.magnitude = magnitude;
    }

    public Double getTime() {
        return this.time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Double getMagnitude() {
        return this.magnitude;
    }

    public void setMagnitude(Double magnitude) {
        this.magnitude = magnitude;
    }

    @Override
    public String toString() {
        return "{" +
                " time='" + getTime() + "'" +
                ", magnitude='" + getMagnitude() + "'" +
                "}";
    }

}
