package com.example.finalproject.services;

import java.util.List;

import com.example.finalproject.model.Names;

public interface NamesService {
    List<Names> getAllNames();
    boolean deleteById(int id);
}
