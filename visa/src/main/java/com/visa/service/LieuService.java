package com.visa.service;

import com.visa.entity.Lieu;
import java.util.List;
import java.util.Optional;

public interface LieuService {
    List<Lieu> findAll();
    Optional<Lieu> findById(Integer id);
    Lieu save(Lieu lieu);
    void deleteById(Integer id);
}
