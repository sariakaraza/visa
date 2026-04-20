package com.visa.service;

import com.visa.entity.Demande;
import java.util.List;
import java.util.Optional;

public interface DemandeService {
    List<Demande> findAll();
    Optional<Demande> findById(Integer id);
    Demande save(Demande demande);
    void deleteById(Integer id);
}
