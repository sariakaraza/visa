package com.visa.service;

import com.visa.entity.StatutDemande;
import java.util.List;
import java.util.Optional;

public interface StatutDemandeService {
    List<StatutDemande> findAll();
    Optional<StatutDemande> findById(Integer id);
    StatutDemande save(StatutDemande statutDemande);
    void deleteById(Integer id);
}
