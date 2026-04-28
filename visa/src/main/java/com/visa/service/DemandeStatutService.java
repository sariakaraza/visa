package com.visa.service;

import com.visa.entity.Demande;
import com.visa.entity.DemandeStatut;
import java.util.List;
import java.util.Optional;

public interface DemandeStatutService {
    List<DemandeStatut> findAll();
    Optional<DemandeStatut> findById(Integer id);
    DemandeStatut save(DemandeStatut demandeStatut);
    void deleteById(Integer id);
    List<DemandeStatut> findByDemande(Demande demande);
}
