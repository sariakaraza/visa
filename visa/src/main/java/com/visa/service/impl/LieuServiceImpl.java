package com.visa.service.impl;

import com.visa.entity.Lieu;
import com.visa.repository.LieuRepository;
import com.visa.service.LieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LieuServiceImpl implements LieuService {

    @Autowired
    private LieuRepository lieuRepository;

    @Override
    public List<Lieu> findAll() {
        return lieuRepository.findAll();
    }

    @Override
    public Optional<Lieu> findById(Integer id) {
        return lieuRepository.findById(id);
    }

    @Override
    public Lieu save(Lieu lieu) {
        return lieuRepository.save(lieu);
    }

    @Override
    public void deleteById(Integer id) {
        lieuRepository.deleteById(id);
    }
}
