package com.visa.repository;

import com.visa.entity.Demande;
import com.visa.entity.DemandeStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeStatutRepository extends JpaRepository<DemandeStatut, Integer> {
    List<DemandeStatut> findByDemande(Demande demande);
}
