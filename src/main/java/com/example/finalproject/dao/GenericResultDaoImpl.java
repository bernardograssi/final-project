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

@Repository
public class GenericResultDaoImpl extends JdbcDaoSupport implements GenericResultDao {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<GenericResult> getDataByName(String name) {
        String sql = "{CALL getDataByName(?)};";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql, name);

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

        return result;
    }

    @Override
    public List<GenericResult> getAllData() {
        String sql = "{CALL getAllData()};";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

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

        return result;
    }

    @Override
    public boolean resetDatabase() {
        String sqlReset = "{CALL resetDatabase()};";
        int update = getJdbcTemplate().update(sqlReset);

        return true;
    }

}
