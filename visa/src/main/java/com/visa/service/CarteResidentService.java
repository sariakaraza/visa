package com.visa.service;

import com.visa.entity.CarteResident;
import java.util.List;
import java.util.Optional;

public interface CarteResidentService {
    List<CarteResident> findAll();
    Optional<CarteResident> findById(Integer id);
    CarteResident save(CarteResident carteResident);
    void deleteById(Integer id);
}
