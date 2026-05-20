package com.visa.service.impl;

import com.visa.entity.StatutDemande;
import com.visa.entity.StatutDemande;
import com.visa.repository.StatutDemandeRepository;
import com.visa.service.StatutDemandeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatutDemandeServiceImpl implements StatutDemandeService {

    private final StatutDemandeRepository repository;

    public StatutDemandeServiceImpl(StatutDemandeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<StatutDemande> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<StatutDemande> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public StatutDemande save(StatutDemande StatutDemande) {
        return repository.save(StatutDemande);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public StatutDemande findByLibelle(String libelle){
        return repository.findByLibelle(libelle);
    }
}