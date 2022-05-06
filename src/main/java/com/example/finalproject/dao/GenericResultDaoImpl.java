package com.example.finalproject.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import com.example.finalproject.dao.interfaces.GenericResultDao;
import com.example.finalproject.model.GenericResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * This class implements the methods related to the GenericResult object.
 */
@Repository
public class GenericResultDaoImpl extends JdbcDaoSupport implements GenericResultDao {

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
     * This method calls the getDataByName stored procedure and parses the result,
     * which is then returned to the caller as a list.
     * 
     * @return list of GenericResult objects.
     */
    @Override
    public List<GenericResult> getDataByName(String name) {
        String sql = "{CALL getDataByName(?)};";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql, name); // Call the stored procedure.

        // For each row of the result from the stored procedure, add it to the list.
        List<GenericResult> result = new ArrayList<GenericResult>();
        for (Map<String, Object> row : rows) {
            GenericResult genRes = new GenericResult(
                    (String) row.get("name"),
                    (double) row.get("band_max"),
                    (double) row.get("band_min"),
                    (double) row.get("delta_value"),
                    (double) row.get("mag_curve"),
                    (double) row.get("lum_curve"),
                    (double) row.get("magnitude"),
                    (double) row.get("luminosity"),
                    (double) row.get("time_value"),
                    (double) row.get("calc_res_1"),
                    (double) row.get("calc_res_2"));
            result.add(genRes);
        }

        // Return list of GenericResult objects.
        return result;
    }

    /**
     * This method calls the getAllData stored procedure, parses the result and
     * returns as a list to the caller.
     * 
     * @return list of GenericResult objects.
     */
    @Override
    public List<GenericResult> getAllData() {
        String sql = "{CALL getAllData()};";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql); // Call stored procedure.

        // For each row of the result from the stored procedure, add it to the list.
        List<GenericResult> result = new ArrayList<GenericResult>();
        for (Map<String, Object> row : rows) {
            GenericResult genRes = new GenericResult(
                    (String) row.get("name"),
                    (double) row.get("band_max"),
                    (double) row.get("band_min"),
                    (double) row.get("delta_value"),
                    (double) row.get("mag_curve"),
                    (double) row.get("lum_curve"),
                    (double) row.get("magnitude"),
                    (double) row.get("luminosity"),
                    (double) row.get("time_value"),
                    (double) row.get("calc_res_1"),
                    (double) row.get("calc_res_2"));
            result.add(genRes);
        }

        // Return list of GenericResult objects.
        return result;
    }

    /**
     * This method calls the resetDatabase stored procedure.
     * 
     * @return true if the stored procedure modifies more than zero rows, false
     *         otherwise.
     */
    @Override
    public boolean resetDatabase() {
        String sqlReset = "{CALL resetDatabase()};";
        int update = getJdbcTemplate().update(sqlReset); // Call stored procedure.

        // Return true if more than zero rows are modified, false otherwise.
        return update > 0;
    }

}
