package com.visa.service.impl;

import com.visa.entity.SituationFamiliale;
import com.visa.repository.SituationFamilialeRepository;
import com.visa.service.SituationFamilialeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SituationFamilialeServiceImpl implements SituationFamilialeService {

    private final SituationFamilialeRepository situationFamilialeRepository;

    public SituationFamilialeServiceImpl(SituationFamilialeRepository situationFamilialeRepository) {
        this.situationFamilialeRepository = situationFamilialeRepository;
    }

    @Override
    public List<SituationFamiliale> findAll() {
        return situationFamilialeRepository.findAll();
    }

    @Override
    public Optional<SituationFamiliale> findById(Integer id) {
        return situationFamilialeRepository.findById(id);
    }

    @Override
    public SituationFamiliale save(SituationFamiliale situationFamiliale) {
        return situationFamilialeRepository.save(situationFamiliale);
    }

    @Override
    public void deleteById(Integer id) {
        situationFamilialeRepository.deleteById(id);
    }
}