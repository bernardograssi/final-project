package com.example.finalproject.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * This class is represents a CSV file data.
 */
public class CSVData {

    // Attribute that represents the supernova name.
    @NotBlank
    private String name;

    // Attribute that represents the Response values.
    @NotEmpty
    private List<Response> values;

    /**
     * Class constructor.
     * 
     * @param name   name of the supernova.
     * @param values Response values from file.
     */
    public CSVData(String name, List<Response> values) {
        this.name = name;
        this.values = values;
    }

    /**
     * This method returns the name of the supernova.
     * 
     * @return name of the supernova.
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method returns the Response values.
     * 
     * @return the Response values.
     */
    public List<Response> getValues() {
        return this.values;
    }

    /**
     * This method converts the object to string.
     */
    @Override
    public String toString() {
        return "{" +
                " name='" + getName() + "'" +
                ", values='" + getValues() + "'" +
                "}";
    }

}
