package com.visa.service.impl;

import com.visa.entity.PieceJustificative;
import com.visa.repository.PieceJustificativeRepository;
import com.visa.service.PieceJustificativeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PieceJustificativeServiceImpl implements PieceJustificativeService {

    private final PieceJustificativeRepository pieceJustificativeRepository;

    public PieceJustificativeServiceImpl(PieceJustificativeRepository pieceJustificativeRepository) {
        this.pieceJustificativeRepository = pieceJustificativeRepository;
    }

    @Override
    public List<PieceJustificative> findAll() {
        return pieceJustificativeRepository.findAll();
    }

    @Override
    public Optional<PieceJustificative> findById(Integer id) {
        return pieceJustificativeRepository.findById(id);
    }

    @Override
    public PieceJustificative save(PieceJustificative pieceJustificative) {
        return pieceJustificativeRepository.save(pieceJustificative);
    }

    @Override
    public void deleteById(Integer id) {
        pieceJustificativeRepository.deleteById(id);
    }
}