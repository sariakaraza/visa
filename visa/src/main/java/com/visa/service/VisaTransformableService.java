package com.visa.service;

import com.visa.entity.VisaTransformable;
import java.util.List;
import java.util.Optional;

public interface VisaTransformableService {
    List<VisaTransformable> findAll();
    Optional<VisaTransformable> findById(Integer id);
    VisaTransformable save(VisaTransformable visaTransformable);
    void deleteById(Integer id);
}
