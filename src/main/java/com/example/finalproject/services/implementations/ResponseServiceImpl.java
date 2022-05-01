package com.example.finalproject.services.implementations;

import com.example.finalproject.dao.interfaces.ResponseDao;
import com.example.finalproject.model.CSVData;
import com.example.finalproject.services.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResponseServiceImpl implements ResponseService {
    @Autowired
    ResponseDao responseDao;

    @Override
    public Boolean insertData(CSVData data) {
        responseDao.insertData(data);
        return null;
    }

    
}
