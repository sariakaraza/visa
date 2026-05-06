package com.visa.repository;

import com.visa.entity.Passeport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasseportRepository extends JpaRepository<Passeport, Integer> {
	Optional<Passeport> findByNumeroIgnoreCase(String numero);
}
