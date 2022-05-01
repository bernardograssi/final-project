package com.example.finalproject.model;

public class GenericResult implements java.io.Serializable {

    private String name;
    private Double bandMax;
    private Double bandMin;
    private Double delta_value;
    private Double aum;
    private Double aul;
    private Double magnitude;
    private Double luminosity;
    private Double time_value;
    private Double calc_res_1;
    private Double calc_res_2;

    public GenericResult(String name, Double bandMax, Double bandMin, Double delta_value, Double aum, Double aul,
            Double magnitude, Double luminosity, Double time_value, Double calc_res_1, Double calc_res_2) {
        this.name = name;
        this.bandMax = bandMax;
        this.bandMin = bandMin;
        this.delta_value = delta_value;
        this.aum = aum;
        this.aul = aul;
        this.magnitude = magnitude;
        this.luminosity = luminosity;
        this.time_value = time_value;
        this.calc_res_1 = calc_res_1;
        this.calc_res_2 = calc_res_2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBandMax() {
        return this.bandMax;
    }

    public void setBandMax(Double bandMax) {
        this.bandMax = bandMax;
    }

    public Double getBandMin() {
        return this.bandMin;
    }

    public void setBandMin(Double bandMin) {
        this.bandMin = bandMin;
    }

    public Double getDelta_value() {
        return this.delta_value;
    }

    public void setDelta_value(Double delta_value) {
        this.delta_value = delta_value;
    }

    public Double getAum() {
        return this.aum;
    }

    public void setAum(Double aum) {
        this.aum = aum;
    }

    public Double getAul() {
        return this.aul;
    }

    public void setAul(Double aul) {
        this.aul = aul;
    }

    public Double getMagnitude() {
        return this.magnitude;
    }

    public void setMagnitude(Double magnitude) {
        this.magnitude = magnitude;
    }

    public Double getLuminosity() {
        return this.luminosity;
    }

    public void setLuminosity(Double luminosity) {
        this.luminosity = luminosity;
    }

    public Double getTime_value() {
        return this.time_value;
    }

    public void setTime_value(Double time_value) {
        this.time_value = time_value;
    }

    public Double getCalc_res_1() {
        return this.calc_res_1;
    }

    public void setCalc_res_1(Double calc_res_1) {
        this.calc_res_1 = calc_res_1;
    }

    public Double getCalc_res_2() {
        return this.calc_res_2;
    }

    public void setCalc_res_2(Double calc_res_2) {
        this.calc_res_2 = calc_res_2;
    }

    @Override
    public String toString() {
        return this.name + ": " + String.valueOf(this.magnitude);
    }

}
