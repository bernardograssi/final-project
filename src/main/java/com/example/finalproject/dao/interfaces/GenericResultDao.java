package com.example.finalproject.dao.interfaces;

import java.util.List;

import com.example.finalproject.model.GenericResult;

/**
 * Interface for GenericResultDaoImpl.
 */
public interface GenericResultDao {
    // Methods.
    
    List<GenericResult> getAllData();

    List<GenericResult> getDataByName(String name);

    boolean resetDatabase();
}
