package com.visa.service;

import com.visa.entity.PieceJustificative;
import java.util.List;
import java.util.Optional;

public interface PieceJustificativeService {
    List<PieceJustificative> findAll();
    Optional<PieceJustificative> findById(Integer id);
    PieceJustificative save(PieceJustificative pieceJustificative);
    void deleteById(Integer id);
}