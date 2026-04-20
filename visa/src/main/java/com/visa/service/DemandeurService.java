package com.visa.service;

import com.visa.entity.Demandeur;
import java.util.List;
import java.util.Optional;

public interface DemandeurService {
    List<Demandeur> findAll();
    Optional<Demandeur> findById(Integer id);
    Demandeur save(Demandeur demandeur);
    void deleteById(Integer id);
}
