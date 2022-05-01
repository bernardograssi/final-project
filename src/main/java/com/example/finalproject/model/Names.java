package com.example.finalproject.model;

public class Names {

    private int id;
    private String name;

    public Names(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int tid) {
        this.id = tid;
    }

    public void setName(String tname) {
        this.name = tname;
    }

    @Override
    public String toString(){
        return String.valueOf(this.id) + ": " + this.name;
    }
}
