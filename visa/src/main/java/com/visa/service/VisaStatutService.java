package com.visa.service;

import com.visa.entity.VisaStatut;
import java.util.List;
import java.util.Optional;

public interface VisaStatutService {
    List<VisaStatut> findAll();
    Optional<VisaStatut> findById(Integer id);
    VisaStatut save(VisaStatut visaStatut);
    void deleteById(Integer id);
}
