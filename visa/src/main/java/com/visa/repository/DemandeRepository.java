package com.visa.repository;

import com.visa.entity.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Integer> {
	Optional<Demande> findByReferenceDemandeIgnoreCase(String referenceDemande);
}
