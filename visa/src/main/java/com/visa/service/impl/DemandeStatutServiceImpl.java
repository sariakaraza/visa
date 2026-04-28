package com.visa.service.impl;

import com.visa.entity.Demande;
import com.visa.entity.DemandeStatut;
import com.visa.repository.DemandeStatutRepository;
import com.visa.service.DemandeStatutService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DemandeStatutServiceImpl implements DemandeStatutService {

    private final DemandeStatutRepository demandeStatutRepository;

    public DemandeStatutServiceImpl(DemandeStatutRepository demandeStatutRepository) {
        this.demandeStatutRepository = demandeStatutRepository;
    }

    @Override
    public List<DemandeStatut> findAll() {
        return demandeStatutRepository.findAll();
    }

    @Override
    public Optional<DemandeStatut> findById(Integer id) {
        return demandeStatutRepository.findById(id);
    }

    @Override
    public DemandeStatut save(DemandeStatut demandeStatut) {
        return demandeStatutRepository.save(demandeStatut);
    }

    @Override
    public void deleteById(Integer id) {
        demandeStatutRepository.deleteById(id);
    }

    @Override
    public List<DemandeStatut> findByDemande(Demande demande) {
        return demandeStatutRepository.findByDemande(demande);
    }
}