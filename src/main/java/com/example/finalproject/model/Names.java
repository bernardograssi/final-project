package com.example.finalproject.model;

/**
 * This class is an object representation of the data from the sn_names table.
 */
public class Names {

    private int id; // The id of the supernova.
    private String name; // The name of the supernova.

    /**
     * Class constructor.
     * @param id the id of the supernova.
     * @param name the name of the supernova.
     */
    public Names(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * This method returns the id of the supernova.
     * @return the id of the supernova.
     */
    public int getId() {
        return id;
    }

    /**
     * This method returns the name of the supernova.
     * @return the name of the supernova.
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns a string representation of the object.
     */
    @Override
    public String toString() {
        return String.valueOf(this.id) + ": " + this.name;
    }
}
