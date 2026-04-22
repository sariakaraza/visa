package com.visa.service.impl;

import com.visa.entity.TypeVisa;
import com.visa.repository.TypeVisaRepository;
import com.visa.service.TypeVisaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeVisaServiceImpl implements TypeVisaService {

    private final TypeVisaRepository typeVisaRepository;

    public TypeVisaServiceImpl(TypeVisaRepository typeVisaRepository) {
        this.typeVisaRepository = typeVisaRepository;
    }

    @Override
    public List<TypeVisa> findAll() {
        return typeVisaRepository.findAll();
    }

    @Override
    public Optional<TypeVisa> findById(Integer id) {
        return typeVisaRepository.findById(id);
    }

    @Override
    public TypeVisa save(TypeVisa typeVisa) {
        return typeVisaRepository.save(typeVisa);
    }

    @Override
    public void deleteById(Integer id) {
        typeVisaRepository.deleteById(id);
    }
}