package com.visa.service.impl;

import com.visa.entity.Demandeur;
import com.visa.entity.Dossier;
import com.visa.entity.PieceJustificative;
import com.visa.repository.DemandeurRepository;
import com.visa.repository.DossierRepository;
import com.visa.repository.PieceJustificativeRepository;
import com.visa.service.PieceJustificativeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Service
public class PieceJustificativeServiceImpl implements PieceJustificativeService {

    private final PieceJustificativeRepository pieceJustificativeRepository;
    private final DossierRepository dossierRepository;
    private final DemandeurRepository demandeurRepository;

    private final String uploadBaseDir;

    public PieceJustificativeServiceImpl(
            PieceJustificativeRepository pieceJustificativeRepository,
            DossierRepository dossierRepository,
            DemandeurRepository demandeurRepository,
            @Value("${app.upload.base-dir:uploads}") String uploadBaseDir
    ) {
        this.pieceJustificativeRepository = pieceJustificativeRepository;
        this.dossierRepository = dossierRepository;
        this.demandeurRepository = demandeurRepository;
        this.uploadBaseDir = uploadBaseDir;
    }

    @Override
    public List<PieceJustificative> findAll() {
        return pieceJustificativeRepository.findAll();
    }

    @Override
    public Optional<PieceJustificative> findById(Integer id) {
        return pieceJustificativeRepository.findById(id);
    }

    @Override
    public PieceJustificative save(PieceJustificative pieceJustificative) {
        return pieceJustificativeRepository.save(pieceJustificative);
    }

    @Override
    public void deleteById(Integer id) {
        pieceJustificativeRepository.deleteById(id);
    }

    @Override
    public PieceJustificative uploadAndSavePieceJustificative(MultipartFile file, Integer idDossier, Integer idDemandeur) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Fichier obligatoire");
        }
        if (idDossier == null) {
            throw new IllegalArgumentException("idDossier obligatoire");
        }
        if (idDemandeur == null) {
            throw new IllegalArgumentException("idDemandeur obligatoire");
        }

        Dossier dossier = dossierRepository.findById(idDossier)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable: " + idDossier));
        Demandeur demandeur = demandeurRepository.findById(idDemandeur)
                .orElseThrow(() -> new RuntimeException("Demandeur introuvable: " + idDemandeur));

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null) {
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex >= 0 && dotIndex < originalFilename.length() - 1) {
                extension = originalFilename.substring(dotIndex);
            }
        }

        String uniqueName = UUID.randomUUID() + extension;
        String relativePath = Paths.get(uploadBaseDir, "pieces", uniqueName).toString().replace('\\', '/');
        Path targetPath = Paths.get(System.getProperty("user.dir")).resolve(relativePath);

        try {
            Files.createDirectories(targetPath.getParent());
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du fichier", e);
        }

        PieceJustificative piece = new PieceJustificative();
        piece.setDateAjout(new Timestamp(System.currentTimeMillis()));
        piece.setDossier(dossier);
        piece.setDemandeur(demandeur);
        piece.setCheminFichier(relativePath);

        return pieceJustificativeRepository.save(piece);
    }

    @Override
    public List<PieceJustificative> findByDemandeurAndDossier(Integer idDemandeur, Integer idDossier) {
        if (idDemandeur == null || idDossier == null) {
            throw new IllegalArgumentException("idDemandeur et idDossier obligatoires");
        }
        return pieceJustificativeRepository.findByDemandeur_IdDemandeurAndDossier_IdDossier(idDemandeur, idDossier);
    }
}