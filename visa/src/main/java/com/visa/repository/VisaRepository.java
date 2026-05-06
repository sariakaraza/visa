package com.visa.repository;

import com.visa.entity.Visa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisaRepository extends JpaRepository<Visa, Integer> {
	Optional<Visa> findFirstByPasseport_NumeroIgnoreCaseOrderByDemande_DateDemandeDescIdVisaDesc(String numero);
}
