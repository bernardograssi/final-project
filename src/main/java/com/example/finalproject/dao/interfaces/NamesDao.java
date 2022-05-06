package com.example.finalproject.dao.interfaces;

import java.util.List;

import com.example.finalproject.model.Names;

/**
 * Interface for NamesDao.
 */
public interface NamesDao {
    // Methods.

    List<Names> getAllNames();

    boolean deleteById(int id);
}