package com.example.finalproject.model;

import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class CSVData {
    @NotBlank
    private String name;

    @NotEmpty
    private List<Response> values;

    public CSVData(String name, List<Response> values) {
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Response> getValues() {
        return this.values;
    }

    public void setValues(List<Response> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "{" +
                " name='" + getName() + "'" +
                ", values='" + getValues() + "'" +
                "}";
    }

}
