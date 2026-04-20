package com.visa.service;

import com.visa.entity.StatutVisa;
import java.util.List;
import java.util.Optional;

public interface StatutVisaService {
    List<StatutVisa> findAll();
    Optional<StatutVisa> findById(Integer id);
    StatutVisa save(StatutVisa statutVisa);
    void deleteById(Integer id);
}
