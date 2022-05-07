package com.example.finalproject.model;

/**
 * This class is an object representation of the data transported from the
 * client to the server side that contains the magnitude and time values.
 */
public class Response {
    private Double time; // Time value.
    private Double magnitude; // Magnitude value.

    /**
     * Class constructor.
     * 
     * @param time      time value.
     * @param magnitude magnitude value.
     */
    public Response(Double time, Double magnitude) {
        this.time = time;
        this.magnitude = magnitude;
    }

    /**
     * This method returns the time value.
     * 
     * @return the time value.
     */
    public Double getTime() {
        return this.time;
    }

    /**
     * This method sets the time value.
     * 
     * @param time the time value.
     */
    public void setTime(Double time) {
        this.time = time;
    }

    /**
     * This method returns the magnitude value.
     * 
     * @return the magnitude value.
     */
    public Double getMagnitude() {
        return this.magnitude;
    }

    /**
     * This method sets the magnitude value.
     * 
     * @param magnitude the magnitude value.
     */
    public void setMagnitude(Double magnitude) {
        this.magnitude = magnitude;
    }

    /**
     * This method returns a string representation of the object.
     */
    @Override
    public String toString() {
        return "{" +
                " time='" + getTime() + "'" +
                ", magnitude='" + getMagnitude() + "'" +
                "}";
    }

}
