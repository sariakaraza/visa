package com.visa.service.impl;

import com.visa.entity.TypeDemande;
import com.visa.repository.TypeDemandeRepository;
import com.visa.service.TypeDemandeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeDemandeServiceImpl implements TypeDemandeService {

    private final TypeDemandeRepository typeDemandeRepository;

    public TypeDemandeServiceImpl(TypeDemandeRepository typeDemandeRepository) {
        this.typeDemandeRepository = typeDemandeRepository;
    }

    @Override
    public List<TypeDemande> findAll() {
        return typeDemandeRepository.findAll();
    }

    @Override
    public Optional<TypeDemande> findById(Integer id) {
        return typeDemandeRepository.findById(id);
    }

    @Override
    public TypeDemande save(TypeDemande typeDemande) {
        return typeDemandeRepository.save(typeDemande);
    }

    @Override
    public void deleteById(Integer id) {
        typeDemandeRepository.deleteById(id);
    }
}