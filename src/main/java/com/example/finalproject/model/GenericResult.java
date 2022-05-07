package com.example.finalproject.model;

/**
 * This class represents the GenericResult object, which results from the joins
 * of all the tables in the database.
 */
public class GenericResult implements java.io.Serializable {

    private String name; // Name of the supernova.
    private Double bandMax; // Maximum value of the magnitudes.
    private Double bandMin; // Minimum value of the magnitudes.
    private Double delta_value; // Delta value of the magnitudes.
    private Double aum; // The area under the magnitude curve.
    private Double aul; // The area under the luminosity curve.
    private Double magnitude; // The magnitude value.
    private Double luminosity; // The luminosity value.
    private Double time_value; // The time value.
    private Double calc_res_1; // The 1st calculation: delta/max.
    private Double calc_res_2; // The 2nd calculation: (x - min)/max for x in magnitudes.

    /**
     * Class constructor.
     * 
     * @param name        name of the supernova.
     * @param bandMax     maximum value of the magnitudes.
     * @param bandMin     minimum value of the magnitudes.
     * @param delta_value delta value of the magnitudes.
     * @param aum         area under the magnitude curve.
     * @param aul         area under the luminosity curve.
     * @param magnitude   magnitude value.
     * @param luminosity  luminosity value.
     * @param time_value  time value.
     * @param calc_res_1  delta/max.
     * @param calc_res_2  (x - min)/max for x in magnitudes.
     */
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

    /**
     * This method returns the name of the supernova.
     * 
     * @return the name of the supernova.
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method returns the maximum value of the magnitudes.
     * 
     * @return the maximum value of the magnitudes.
     */
    public Double getBandMax() {
        return this.bandMax;
    }

    /**
     * This method returns the minimum value of the magnitudes.
     * 
     * @return the minimum value of the magnitudes.
     */
    public Double getBandMin() {
        return this.bandMin;
    }

    /**
     * This method returns the delta value of the magnitudes.
     * 
     * @return the delta value of the magnitudes.
     */
    public Double getDelta_value() {
        return this.delta_value;
    }

    /**
     * This method returns the value of the area under the magnitude curve.
     * 
     * @return the value of the area under the magnitude curve.
     */
    public Double getAum() {
        return this.aum;
    }

    /**
     * This method returns the value of the area under the luminosity curve.
     * 
     * @return the value of the area under the luminosity curve.
     */
    public Double getAul() {
        return this.aul;
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
     * This method returns the luminosity value.
     * 
     * @return the luminosity value.
     */
    public Double getLuminosity() {
        return this.luminosity;
    }

    /**
     * This method returns the time value.
     * 
     * @return the time value.
     */
    public Double getTime_value() {
        return this.time_value;
    }

    /**
     * This method returns the value of delta/max.
     * 
     * @return the value of delta/max.
     */
    public Double getCalc_res_1() {
        return this.calc_res_1;
    }

    /**
     * This method returns the value of (x - min)/max for x in magnitudes.
     * @return the value of (x - min)/max for x in magnitudes.
     */
    public Double getCalc_res_2() {
        return this.calc_res_2;
    }

    /**
     * This method converts the object to a string.
     */
    @Override
    public String toString() {
        return this.name + ": " + String.valueOf(this.magnitude);
    }

}
