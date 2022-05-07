package com.example.finalproject.services.implementations;

import java.io.IOException;

import com.example.finalproject.dao.interfaces.ResponseDao;
import com.example.finalproject.model.CSVData;
import com.example.finalproject.services.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class represents the implementation of the Response services interface.
 */
@Service
public class ResponseServiceImpl implements ResponseService {
    // Wire this class to the ResponseDao class.
    @Autowired
    ResponseDao responseDao;

    /**
     * This method calls the insertData from ResponseDao.
     */
    @Override
    public Boolean insertData(CSVData data) {
        try {
            responseDao.insertData(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
