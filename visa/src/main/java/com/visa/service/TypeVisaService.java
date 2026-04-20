package com.visa.service;

import com.visa.entity.TypeVisa;
import java.util.List;
import java.util.Optional;

public interface TypeVisaService {
    List<TypeVisa> findAll();
    Optional<TypeVisa> findById(Integer id);
    TypeVisa save(TypeVisa typeVisa);
    void deleteById(Integer id);
}
