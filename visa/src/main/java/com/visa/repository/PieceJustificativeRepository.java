package com.visa.repository;

import com.visa.entity.PieceJustificative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PieceJustificativeRepository extends JpaRepository<PieceJustificative, Integer> {
}