package com.visa.service;

import com.visa.entity.Visa;
import java.util.List;
import java.util.Optional;

public interface VisaService {
    List<Visa> findAll();
    Optional<Visa> findById(Integer id);
    Visa save(Visa visa);
    void deleteById(Integer id);
}
