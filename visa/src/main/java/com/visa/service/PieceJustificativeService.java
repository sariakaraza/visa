package com.visa.service;

import com.visa.entity.PieceJustificative;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface PieceJustificativeService {
    List<PieceJustificative> findAll();
    Optional<PieceJustificative> findById(Integer id);
    PieceJustificative save(PieceJustificative pieceJustificative);
    void deleteById(Integer id);

    PieceJustificative uploadAndSavePieceJustificative(MultipartFile file, Integer idDossier, Integer idDemandeur);

    List<PieceJustificative> findByDemandeurAndDossier(Integer idDemandeur, Integer idDossier);
}