package com.visa.service;

import com.visa.entity.Passeport;
import java.util.List;
import java.util.Optional;

public interface PasseportService {
    List<Passeport> findAll();
    Optional<Passeport> findById(Integer id);
    Passeport save(Passeport passeport);
    void deleteById(Integer id);
}
