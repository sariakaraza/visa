package com.visa.repository;

import com.visa.entity.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutDemandeRepository extends JpaRepository<StatutDemande, Integer> {
}
