package com.example.finalproject.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import com.example.finalproject.Executer;
import com.example.finalproject.dao.interfaces.ResponseDao;
import com.example.finalproject.model.CSVData;
import com.example.finalproject.model.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * This class implements the methods related to the ResponseDao object.
 */
@Repository
public class ResponseDaoImpl extends JdbcDaoSupport implements ResponseDao {
    /**
     * Data Source object that connects to the MySQL database.
     */
    @Autowired
    DataSource dataSource;

    /**
     * Data Source object initializer.
     */
    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    /**
     * This method inserts data from the CSVData object, which originates from a
     * .csv file submitted by the user.
     * 
     * @return true if all operations were successful, false otherwise.
     */
    @Override
    public Boolean insertData(CSVData data) {
        // Stored procedures to insert to sn_names.
        String sqlInsert = "{CALL insertName(?)};";
        String sqlId = "{CALL getIdByName(?)};";

        // Get name and values of the new supernova.
        String name = data.getName();
        List<Response> values = data.getValues();

        // Insert into sn_names.
        int update = getJdbcTemplate().update(sqlInsert, name);
        int nameId = (int) getJdbcTemplate().queryForList(sqlId, name).get(0).get("id");

        // Insert into sn_band_association and sn_time_association.
        boolean tav = insertTimeAndValues(values, nameId);

        // Initialize variables.
        Double band_max = 999999.;
        Double band_min = -999999.;
        Double band_delta = 0.;
        Double magnitudes = 0.;
        Double calc_result_1 = 0.;
        Double calc_result_2 = 0.;
        double x_min_max = 0.;
        int limit = 0;

        // Set band_max and band_min through iteration over the values.
        for (int i = 0; i < values.size(); i++) {
            Double mag = values.get(i).getMagnitude();
            magnitudes += mag;

            // Check for minimum and update.
            if (mag > band_min) {
                band_min = mag;
            }

            // Check for maximum and update.
            if (mag < band_max) {
                band_max = mag;
            }

        }

        // Get calc_res_
        for (int k = 0; k <= limit; k++) {
            x_min_max += Math.abs(((Double.valueOf(values.get(k).getMagnitude().toString())) - band_min) / band_max);
        }

        // Calculate band delta and calc_results.
        band_delta = Math.abs(band_max - band_min);
        calc_result_1 = band_delta / band_max;
        calc_result_2 = x_min_max;

        // Insert into sn_deltas, sn_calc, and sn_curves.
        boolean dac = insertDeltaAndCalc(nameId, band_delta, calc_result_1, calc_result_2);
        boolean cur = insertCurves(nameId);

        // Get last IDs from sn_deltas, sn_curves, and sn_calc.
        String sqlBandId = "{CALL getLastBandId()};";
        int band_delta_id = (int) getJdbcTemplate().queryForList(sqlBandId).get(0).get("maxId");

        String sqlCurveId = "{CALL getLastCurveId()};";
        int curve_id = (int) getJdbcTemplate().queryForList(sqlCurveId).get(0).get("maxId");

        String sqlCalcId = "{CALL getLastCalcId()};";
        int calc_res_id = (int) getJdbcTemplate().queryForList(sqlCalcId).get(0).get("maxId");

        // Insert into sn_results using the IDs previously obtained.
        boolean res = insertResults(nameId, band_max, band_min, band_delta_id, curve_id, calc_res_id);
        Executer exe = new Executer();

        // Run the Python script to update sn_curves.
        exe.run();

        // Returns true if all updates were successful, false otherwise.
        return (res && tav && dac && cur && (update > 0));
    }

    /**
     * This method inserts data into the sn_band_association and sn_time_association
     * tables.
     * 
     * @return true if updates are successful, false otherwise.
     */
    @Override
    public boolean insertTimeAndValues(List<Response> data, int nameId) {
        String sqlInsertTime = "{CALL insertTime(?, ?)};";
        String sqlInsertValues = "{CALL insertValues(?, ?, ?)};";

        // For each Response item (time, magnitude), insert to sn_time_association and
        // sn_band_association.
        for (int i = 0; i < data.size(); i++) {
            Double time = data.get(i).getTime();
            Double magnitude = data.get(i).getMagnitude();
            Double luminosity = (-2.5) * Math.log10(magnitude / (3 * Math.pow(10, 28)));

            int res = getJdbcTemplate().update(sqlInsertTime, time, nameId);
            int res2 = getJdbcTemplate().update(sqlInsertValues, magnitude, luminosity, nameId);
        }

        // Return true if all insertions were successfully done.
        return true;
    }

    /**
     * This method inserts data into the sn_deltas and sn_calc tables.
     * 
     * @return true if updates were successful, false otherwise.
     */
    @Override
    public boolean insertDeltaAndCalc(int nameId, Double band_delta, Double calc_result_1, Double calc_result_2) {
        // Insert into sn_deltas, sn_curves, and sn_calc.
        String sqlInsertDelta = "{CALL insertDelta(?, ?)};";
        int deltaUpdate = getJdbcTemplate().update(sqlInsertDelta, nameId, band_delta);

        String sqlInsertCalc = "{CALL insertCalc(?, ?, ?)};";
        int calcUpdate = getJdbcTemplate().update(sqlInsertCalc, nameId, calc_result_1, calc_result_2);

        return (deltaUpdate > 0) && (calcUpdate > 0);
    }

    /**
     * This method inserts data into the sn_curves table.
     * 
     * @return true if successful update, false otherwise.
     */
    @Override
    public boolean insertCurves(int nameId) {
        Double aum_curve = 0.;
        Double aul_curve = 0.;

        String sqlInsertCurves = "{CALL insertCurves(?, ?, ?)};";
        int curveUpdate = getJdbcTemplate().update(sqlInsertCurves, nameId, aum_curve, aul_curve);

        return curveUpdate > 0;
    }

    /**
     * This method insert data into the sn_results table.
     * 
     * @return true if successful update, false otherwise.
     */
    @Override
    public boolean insertResults(int nameId, Double band_max, Double band_min, int band_delta_id, int curve_id,
            int calc_res_id) {
        String sqlInsertResult = "{CALL insertResults(?, ?, ?, ?, ?, ?)};";
        int updateResult = getJdbcTemplate().update(sqlInsertResult, nameId, band_max, band_min, band_delta_id,
                curve_id, calc_res_id);

        return updateResult > 0;
    }

}
