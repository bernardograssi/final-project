package com.example.finalproject.services;

import java.util.List;

import com.example.finalproject.model.GenericResult;

/**
 * This is the interface of the GenericResult object services.
 */
public interface GenericResultService {
    // Methods definitions.
    List<GenericResult> getAllData();
    List<GenericResult> getDataByName(String name);
    boolean resetDatabase();
}
