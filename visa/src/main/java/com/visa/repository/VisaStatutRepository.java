package com.visa.repository;

import com.visa.entity.VisaStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisaStatutRepository extends JpaRepository<VisaStatut, Integer> {
}
