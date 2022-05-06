package com.example.finalproject.dao.interfaces;

import java.io.IOException;
import java.util.List;

import com.example.finalproject.model.CSVData;
import com.example.finalproject.model.Response;

/**
 * Interface for ResponseDao.
 */
public interface ResponseDao {
    // Methods.

    Boolean insertData(CSVData data) throws IOException;

    boolean insertTimeAndValues(List<Response> data, int nameId);

    boolean insertDeltaAndCalc(int nameId, Double band_delta, Double calc_result_1, Double calc_result_2);

    boolean insertCurves(int nameId);

    boolean insertResults(int nameId, Double band_max, Double band_min, int band_delta_id, int curve_id, int calc_res_id);

}
