package com.example.finalproject.services.implementations;

import java.util.List;

import com.example.finalproject.dao.interfaces.GenericResultDao;
import com.example.finalproject.model.GenericResult;
import com.example.finalproject.services.GenericResultService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenericResultServiceImpl implements GenericResultService{

    @Autowired
    GenericResultDao genericResultDao;

    @Override
    public List<GenericResult> getAllData() {
        List<GenericResult> genResults = genericResultDao.getAllData();
        return genResults;
    }

    @Override
    public List<GenericResult> getDataByName(String name) {
        List<GenericResult> genData = genericResultDao.getDataByName(name);
        return genData;
    }

    @Override
    public boolean resetDatabase() {
        boolean result = genericResultDao.resetDatabase();
        return result;
    }

}
