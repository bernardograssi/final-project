package com.example.finalproject.services.implementations;

import java.util.List;

import com.example.finalproject.dao.interfaces.NamesDao;
import com.example.finalproject.model.Names;
import com.example.finalproject.services.NamesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NamesServiceImpl implements NamesService{

    @Autowired
    NamesDao namesDao;

    @Override
    public List<Names> getAllNames() {
        List<Names> names = namesDao.getAllNames();
        return names;        
    }

    @Override
    public boolean deleteById(int id) {
        boolean res = namesDao.deleteById(id);
        return res;
    }
    
    
}
