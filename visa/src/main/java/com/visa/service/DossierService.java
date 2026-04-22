package com.visa.service;

import java.util.List;
import com.visa.entity.Dossier;
import com.visa.entity.TypeDemande;

public interface DossierService {
    List<Dossier> findAll();
    Dossier findById(Integer id);
    Dossier save(Dossier dossier);
    void delete(Integer id);
    List<Dossier> findByTypeDemande(TypeDemande typeDemande);
}
