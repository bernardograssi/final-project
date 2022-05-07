package com.example.finalproject.services.implementations;

import java.util.List;

import com.example.finalproject.dao.interfaces.GenericResultDao;
import com.example.finalproject.model.GenericResult;
import com.example.finalproject.services.GenericResultService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class represents the implementation of the methods from the GenericResult services interface.
 */
@Service
public class GenericResultServiceImpl implements GenericResultService{

    // Wire this class to the GenericResultDao class.
    @Autowired
    GenericResultDao genericResultDao;

    /**
     * This method calls the getAllData from GenericResultDao class.
     */
    @Override
    public List<GenericResult> getAllData() {
        List<GenericResult> genResults = genericResultDao.getAllData();
        return genResults;
    }

    /**
     * This method calls the getDataByName from GenericResultDao class.
     */
    @Override
    public List<GenericResult> getDataByName(String name) {
        List<GenericResult> genData = genericResultDao.getDataByName(name);
        return genData;
    }

    /**
     * This method calls the resetDatabase from GenericResultDao class.
     */
    @Override
    public boolean resetDatabase() {
        boolean result = genericResultDao.resetDatabase();
        return result;
    }

}
