package com.visa.repository;

import com.visa.entity.PieceJustificative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PieceJustificativeRepository extends JpaRepository<PieceJustificative, Integer> {
	List<PieceJustificative> findByDemandeur_IdDemandeurAndDossier_IdDossier(Integer idDemandeur, Integer idDossier);
}