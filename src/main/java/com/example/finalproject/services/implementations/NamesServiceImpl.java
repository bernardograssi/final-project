package com.example.finalproject.services.implementations;

import java.util.List;

import com.example.finalproject.dao.interfaces.NamesDao;
import com.example.finalproject.model.Names;
import com.example.finalproject.services.NamesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class represents the implementation of the Names services interface.
 */
@Service
public class NamesServiceImpl implements NamesService {

    // Wire this class to the NamesDao class.
    @Autowired
    NamesDao namesDao;

    /**
     * This method calls the getAllNames from NamesDao.
     */
    @Override
    public List<Names> getAllNames() {
        List<Names> names = namesDao.getAllNames();
        return names;
    }

    /**
     * This method calls the deleteById from NamesDao.
     */
    @Override
    public boolean deleteById(int id) {
        boolean res = namesDao.deleteById(id);
        return res;
    }

}
