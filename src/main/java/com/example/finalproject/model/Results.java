package com.example.finalproject.model;

public class Results {
    private int id;
    private int snId;
    private Double bandMax;
    private Double bandMin;
    private int bandDeltaId;
    private int curveId;
    private int calcResId;


    public Results(int id, int snId, Double bandMax, Double bandMin, int bandDeltaId, int curveId, int calcResId) {
        this.id = id;
        this.snId = snId;
        this.bandMax = bandMax;
        this.bandMin = bandMin;
        this.bandDeltaId = bandDeltaId;
        this.curveId = curveId;
        this.calcResId = calcResId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSnId() {
        return this.snId;
    }

    public void setSnId(int snId) {
        this.snId = snId;
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

    public int getBandDeltaId() {
        return this.bandDeltaId;
    }

    public void setBandDeltaId(int bandDeltaId) {
        this.bandDeltaId = bandDeltaId;
    }

    public int getCurveId() {
        return this.curveId;
    }

    public void setCurveId(int curveId) {
        this.curveId = curveId;
    }

    public int getCalcResId() {
        return this.calcResId;
    }

    public void setCalcResId(int calcResId) {
        this.calcResId = calcResId;
    }

    @Override
    public String toString() {
        return "Max: " + String.valueOf(this.bandMax) + ", Min: " + String.valueOf(this.bandMin);
    }

}
