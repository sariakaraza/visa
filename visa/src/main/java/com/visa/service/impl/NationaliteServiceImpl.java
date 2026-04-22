package com.visa.service.impl;

import com.visa.entity.Nationalite;
import com.visa.repository.NationaliteRepository;
import com.visa.service.NationaliteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NationaliteServiceImpl implements NationaliteService {

    private final NationaliteRepository nationaliteRepository;

    public NationaliteServiceImpl(NationaliteRepository nationaliteRepository) {
        this.nationaliteRepository = nationaliteRepository;
    }

    @Override
    public List<Nationalite> findAll() {
        return nationaliteRepository.findAll();
    }

    @Override
    public Optional<Nationalite> findById(Integer id) {
        return nationaliteRepository.findById(id);
    }

    @Override
    public Nationalite save(Nationalite nationalite) {
        return nationaliteRepository.save(nationalite);
    }

    @Override
    public void deleteById(Integer id) {
        nationaliteRepository.deleteById(id);
    }
}