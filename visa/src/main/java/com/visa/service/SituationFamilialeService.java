package com.visa.service;

import com.visa.entity.SituationFamiliale;
import java.util.List;
import java.util.Optional;

public interface SituationFamilialeService {
    List<SituationFamiliale> findAll();
    Optional<SituationFamiliale> findById(Integer id);
    SituationFamiliale save(SituationFamiliale situationFamiliale);
    void deleteById(Integer id);
}
