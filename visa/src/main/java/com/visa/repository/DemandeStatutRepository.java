package com.visa.repository;

import com.visa.entity.DemandeStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandeStatutRepository extends JpaRepository<DemandeStatut, Integer> {
}
