package com.visa.service.impl;

import com.visa.entity.Demandeur;
import com.visa.repository.DemandeurRepository;
import com.visa.service.DemandeurService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DemandeurServiceImpl implements DemandeurService {

    private final DemandeurRepository demandeurRepository;

    public DemandeurServiceImpl(DemandeurRepository demandeurRepository) {
        this.demandeurRepository = demandeurRepository;
    }

    @Override
    public List<Demandeur> findAll() {
        return demandeurRepository.findAll();
    }

    @Override
    public Optional<Demandeur> findById(Integer id) {
        return demandeurRepository.findById(id);
    }

    @Override
    public Demandeur save(Demandeur demandeur) {
        return demandeurRepository.save(demandeur);
    }

    @Override
    public void deleteById(Integer id) {
        demandeurRepository.deleteById(id);
    }
}