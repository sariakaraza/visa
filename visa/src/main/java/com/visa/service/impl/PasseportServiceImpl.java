package com.visa.service.impl;

import com.visa.entity.Passeport;
import com.visa.repository.PasseportRepository;
import com.visa.service.PasseportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PasseportServiceImpl implements PasseportService {

    @Autowired
    private PasseportRepository passeportRepository;

    @Override
    public List<Passeport> findAll() {
        return passeportRepository.findAll();
    }

    @Override
    public Optional<Passeport> findById(Integer id) {
        return passeportRepository.findById(id);
    }

    @Override
    public Passeport save(Passeport passeport) {
        return passeportRepository.save(passeport);
    }

    @Override
    public void deleteById(Integer id) {
        passeportRepository.deleteById(id);
    }
}
