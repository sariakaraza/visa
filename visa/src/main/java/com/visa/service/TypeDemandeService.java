package com.visa.service;

import com.visa.entity.TypeDemande;
import java.util.List;
import java.util.Optional;

public interface TypeDemandeService {
    List<TypeDemande> findAll();
    Optional<TypeDemande> findById(Integer id);
    TypeDemande save(TypeDemande typeDemande);
    void deleteById(Integer id);
}
