package com.visa.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.visa.entity.Dossier;
import com.visa.entity.TypeDemande;

@Repository
public interface DossierRepository extends JpaRepository<Dossier, Integer> {
    List<Dossier> findByTypeDemande(TypeDemande typeDemande);
}
