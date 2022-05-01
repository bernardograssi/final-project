package com.example.finalproject.dao.interfaces;

import java.util.List;

import com.example.finalproject.model.GenericResult;

public interface GenericResultDao {
    List<GenericResult> getAllData();
    List<GenericResult> getDataByName(String name);
    boolean resetDatabase();
}
