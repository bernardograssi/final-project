package com.example.finalproject.dao.interfaces;

import java.util.List;

import com.example.finalproject.model.Names;

public interface NamesDao {
    List<Names> getAllNames();
    boolean deleteById(int id);
}