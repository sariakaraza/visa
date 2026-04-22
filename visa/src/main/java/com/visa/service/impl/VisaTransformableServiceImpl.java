package com.visa.service.impl;

import com.visa.entity.VisaTransformable;
import com.visa.repository.VisaTransformableRepository;
import com.visa.service.VisaTransformableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisaTransformableServiceImpl implements VisaTransformableService {

    @Autowired
    private VisaTransformableRepository visaTransformableRepository;

    @Override
    public List<VisaTransformable> findAll() {
        return visaTransformableRepository.findAll();
    }

    @Override
    public Optional<VisaTransformable> findById(Integer id) {
        return visaTransformableRepository.findById(id);
    }

    @Override
    public VisaTransformable save(VisaTransformable visaTransformable) {
        return visaTransformableRepository.save(visaTransformable);
    }

    @Override
    public void deleteById(Integer id) {
        visaTransformableRepository.deleteById(id);
    }
}
