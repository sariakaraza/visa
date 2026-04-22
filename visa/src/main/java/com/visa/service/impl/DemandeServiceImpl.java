package com.visa.service.impl;

import com.visa.entity.Demande;
import com.visa.entity.DemandeStatut;
import com.visa.entity.StatutDemande;
import com.visa.repository.DemandeRepository;
import com.visa.repository.DemandeStatutRepository;
import com.visa.repository.StatutDemandeRepository;
import com.visa.service.DemandeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DemandeServiceImpl implements DemandeService {

    private final DemandeRepository demandeRepository;
    private final DemandeStatutRepository demandeStatutRepository;
    private final StatutDemandeRepository statutDemandeRepository;

    public DemandeServiceImpl(
            DemandeRepository demandeRepository,
            DemandeStatutRepository demandeStatutRepository,
            StatutDemandeRepository statutDemandeRepository
    ) {
        this.demandeRepository = demandeRepository;
        this.demandeStatutRepository = demandeStatutRepository;
        this.statutDemandeRepository = statutDemandeRepository;
    }

    @Override
    public List<Demande> findAll() {
        return demandeRepository.findAll();
    }

    @Override
    public Optional<Demande> findById(Integer id) {
        return demandeRepository.findById(id);
    }

    @Override
    @Transactional
    public Demande save(Demande demande) {

        Demande savedDemande = demandeRepository.save(demande);

        StatutDemande initialStatus = statutDemandeRepository.findById(1)
                .orElseThrow(() -> new RuntimeException(
                        "Initial status 'Dossier créé' not found. Please check your database."
                ));

        DemandeStatut demandeStatut = new DemandeStatut();
        demandeStatut.setDemande(savedDemande);
        demandeStatut.setStatutDemande(initialStatus);
        demandeStatut.setDateStatut(Date.valueOf(LocalDate.now()));

        demandeStatutRepository.save(demandeStatut);

        return savedDemande;
    }

    @Override
    public void deleteById(Integer id) {
        demandeRepository.deleteById(id);
    }
}