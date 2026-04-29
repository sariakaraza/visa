package com.visa.service;

import com.visa.entity.Demande;
import com.visa.entity.Demandeur;
import com.visa.entity.Passeport;
import com.visa.entity.Visa;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface DemandeService {
    List<Demande> findAll();
    Optional<Demande> findById(Integer id);
    Demande save(Demande demande);
    Demande update(Demande demande);
    void deleteById(Integer id);
    Demande createTransfertSansAnterieur(Demandeur demandeur, Passeport passeport, Visa visa,
                                       Integer idTypeDemande, Integer idTypeVisa, Integer idLieuVisa);

    Demande createDuplicataSansAnterieur(Demandeur demandeur, Passeport passeport, Visa visa,
                                        Integer idTypeDemande, Integer idTypeVisa, Integer idLieuVisa);

    Demande createDuplicataAvecDonneesAnterieur(Demandeur demandeur, Integer idTypeDemande);

    void processUploadsForDemande(Integer idDemande, List<MultipartFile> files, List<Integer> idDossiers);
}
