package com.visa.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.visa.entity.Dossier;
import com.visa.entity.TypeVisa;
import com.visa.repository.DossierRepository;
import com.visa.service.DossierService;

@Service
public class DossierServiceImpl implements DossierService {

    @Autowired
    private DossierRepository dossierRepository;

    @Override
    public List<Dossier> findAll() {
        return dossierRepository.findAll();
    }

    @Override
    public Dossier findById(Integer id) {
        return dossierRepository.findById(id).orElse(null);
    }

    @Override
    public Dossier save(Dossier dossier) {
        return dossierRepository.save(dossier);
    }

    @Override
    public void delete(Integer id) {
        dossierRepository.deleteById(id);
    }

    @Override
    public List<Dossier> findByTypeVisa(TypeVisa typeVisa) {
        return dossierRepository.findByTypeVisa(typeVisa);
    }
}
