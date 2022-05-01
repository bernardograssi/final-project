package com.example.finalproject.services;

import java.util.List;

import com.example.finalproject.model.GenericResult;

public interface GenericResultService {
    List<GenericResult> getAllData();
    List<GenericResult> getDataByName(String name);
    boolean resetDatabase();
}
