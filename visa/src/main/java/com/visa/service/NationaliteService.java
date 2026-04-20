package com.visa.service;

import com.visa.entity.Nationalite;
import java.util.List;
import java.util.Optional;

public interface NationaliteService {
    List<Nationalite> findAll();
    Optional<Nationalite> findById(Integer id);
    Nationalite save(Nationalite nationalite);
    void deleteById(Integer id);
}
