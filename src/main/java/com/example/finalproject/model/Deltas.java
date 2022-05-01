package com.example.finalproject.model;

public class Deltas {
    private int id;
    private int snId;
    private Double deltaValue;

    public Deltas(int id, int snId, Double deltaValue) {
        this.id = id;
        this.snId = snId;
        this.deltaValue = deltaValue;
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

    public Double getDeltaValue() {
        return this.deltaValue;
    }

    public void setDeltaValue(Double deltaValue) {
        this.deltaValue = deltaValue;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + this.id + "'" +
            ", snId='" + this.snId + "'" +
            ", deltaValue='" + this.deltaValue + "'" +
            "}";
    }

}
