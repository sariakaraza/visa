package com.visa.repository;

import com.visa.entity.StatutVisa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutVisaRepository extends JpaRepository<StatutVisa, Integer> {
}
