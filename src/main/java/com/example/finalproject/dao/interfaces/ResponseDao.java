package com.example.finalproject.dao.interfaces;

import java.io.IOException;

import com.example.finalproject.model.CSVData;

public interface ResponseDao {
    Boolean insertData(CSVData data) throws IOException;
}
