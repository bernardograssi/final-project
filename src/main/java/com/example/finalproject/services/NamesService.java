package com.example.finalproject.services;

import java.util.List;

import com.example.finalproject.model.Names;

/**
 * This is the interface of the Names object services.
 */
public interface NamesService {
    // Methods definitions.
    List<Names> getAllNames();
    boolean deleteById(int id);
}
