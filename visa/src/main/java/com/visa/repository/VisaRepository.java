package com.visa.repository;

import com.visa.entity.Visa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisaRepository extends JpaRepository<Visa, Integer> {
}
