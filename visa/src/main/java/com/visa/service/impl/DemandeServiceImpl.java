package com.visa.service.impl;

import com.visa.entity.*;
import com.visa.repository.*;
import com.visa.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DemandeServiceImpl implements DemandeService {

    private final DemandeRepository demandeRepository;
    private final DemandeStatutRepository demandeStatutRepository;
    private final StatutDemandeRepository statutDemandeRepository;

    private final DemandeurRepository demandeurRepository;
    private final PasseportRepository passeportRepository;
    private final VisaRepository visaRepository;
    private final LieuRepository lieuRepository;
    private final TypeDemandeRepository typeDemandeRepository;
    private final TypeVisaRepository typeVisaRepository;
    private final NationaliteRepository nationaliteRepository;
    private final SituationFamilialeRepository situationFamilialeRepository;
    private final CarteResidentRepository carteResidentRepository;
    private final PieceJustificativeService pieceJustificativeService;
    private final DossierRepository dossierRepository;

    public DemandeServiceImpl(
            DemandeRepository demandeRepository,
            DemandeStatutRepository demandeStatutRepository,
            StatutDemandeRepository statutDemandeRepository,
            DemandeurRepository demandeurRepository,
            PasseportRepository passeportRepository,
            VisaRepository visaRepository,
            LieuRepository lieuRepository,
            TypeDemandeRepository typeDemandeRepository,
            TypeVisaRepository typeVisaRepository,
            NationaliteRepository nationaliteRepository,
            SituationFamilialeRepository situationFamilialeRepository,
            CarteResidentRepository carteResidentRepository,
            PieceJustificativeService pieceJustificativeService,
            DossierRepository dossierRepository

    ) {
        this.demandeRepository = demandeRepository;
        this.demandeStatutRepository = demandeStatutRepository;
        this.statutDemandeRepository = statutDemandeRepository;
        this.demandeurRepository = demandeurRepository;
        this.passeportRepository = passeportRepository;
        this.visaRepository = visaRepository;
        this.lieuRepository = lieuRepository;
        this.typeDemandeRepository = typeDemandeRepository;
        this.typeVisaRepository = typeVisaRepository;
        this.nationaliteRepository = nationaliteRepository;
        this.situationFamilialeRepository = situationFamilialeRepository;
        this.carteResidentRepository = carteResidentRepository;
        this.pieceJustificativeService = pieceJustificativeService;
        this.dossierRepository = dossierRepository;

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
    public Optional<Demande> findByReferenceDemande(String referenceDemande) {
        return demandeRepository.findByReferenceDemande(referenceDemande);
    }

    @Override
    public Optional<Demande> findByPasseportNumero(String numero) {
        return demandeRepository.findByPasseportNumero(numero);
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
    @Transactional
    public Demande update(Demande demande) {
        return demandeRepository.save(demande);
    }

    @Override
    public void deleteById(Integer id) {
        demandeRepository.deleteById(id);
    }
    @Override
    @Transactional
    public Demande createTransfertSansAnterieur(Demandeur demandeur, Passeport passeport, Visa visa,
                                               Integer idTypeDemande, Integer idTypeVisa, Integer idLieuVisa) {

        if (demandeur.getNationalite() == null || demandeur.getNationalite().getIdNationalite() == null) {
            throw new RuntimeException("Nationalité requise pour le demandeur");
        }
        Nationalite nat = nationaliteRepository.findById(demandeur.getNationalite().getIdNationalite())
                .orElseThrow(() -> new RuntimeException("Nationalité introuvable"));
        demandeur.setNationalite(nat);

        if (demandeur.getSituationFamiliale() == null || demandeur.getSituationFamiliale().getIdSituationFamiliale() == null) {
            throw new RuntimeException("Situation familiale requise pour le demandeur");
        }
        SituationFamiliale sit = situationFamilialeRepository.findById(demandeur.getSituationFamiliale().getIdSituationFamiliale())
                .orElseThrow(() -> new RuntimeException("Situation familiale introuvable"));
        demandeur.setSituationFamiliale(sit);

        Demandeur savedDemandeur = demandeurRepository.save(demandeur);

        // Sauvegarde Passeport
        passeport.setDemandeur(savedDemandeur);
        Passeport savedPasseport = passeportRepository.save(passeport);

        // Sauvegarde Demande
        Demande demande = new Demande();
        demande.setDateDemande(new java.sql.Timestamp(System.currentTimeMillis()));
        demande.setTypeDemande(typeDemandeRepository.findById(idTypeDemande)
                .orElseThrow(() -> new RuntimeException("TypeDemande introuvable")));
        demande.setTypeVisa(typeVisaRepository.findById(idTypeVisa)
                .orElseThrow(() -> new RuntimeException("TypeVisa introuvable")));
        demande.setDemandeur(savedDemandeur);

        Demande savedDemande = demandeRepository.save(demande);

        StatutDemande initialStatus = statutDemandeRepository.findByLibelle("Visa approuvé");

        DemandeStatut demandeStatut = new DemandeStatut();
        demandeStatut.setDemande(savedDemande);
        demandeStatut.setStatutDemande(initialStatus);
        demandeStatut.setDateStatut(Date.valueOf(LocalDate.now()));
        demandeStatutRepository.save(demandeStatut);

        // Sauvegarde Visa
        visa.setPasseport(savedPasseport);
        if (idLieuVisa != null) {
            Lieu lieu = lieuRepository.findById(idLieuVisa)
                    .orElseThrow(() -> new RuntimeException("Lieu introuvable"));
            visa.setLieu(lieu);
        } else if (visa.getLieu() == null) {
            throw new RuntimeException("Le lieu du visa est requis");
        }
        visa.setDemande(savedDemande);
        visaRepository.save(visa);

        CarteResident carteResident = new CarteResident();
        carteResident.setDemande(savedDemande);
        carteResident.setPasseport(savedPasseport);
        carteResident.setDateDebut(visa.getDateDebut());
        carteResident.setDateFin(visa.getDateFin());
        carteResident.setReference(visa.getReference());

        carteResidentRepository.save(carteResident);

        return savedDemande;
    }

    @Override
    @Transactional
    public Demande createDuplicataSansAnterieur(Demandeur demandeur, Passeport passeport, Visa visa,
                                                Integer idTypeDemande, Integer idTypeVisa, Integer idLieuVisa) {
        // The duplicata flow is similar to transfert: create demandeur, passeport, demande, visa, statut
        return createTransfertSansAnterieur(demandeur, passeport, visa, idTypeDemande, idTypeVisa, idLieuVisa);
    }

    @Override
    @Transactional
    public Demande createDuplicataAvecDonneesAnterieur(Demandeur demandeur, Integer idTypeDemande) {
        // Récupère le dernier passeport du demandeur
        Passeport dernierePasseport = passeportRepository.findAll().stream()
                .filter(p -> p.getDemandeur().getIdDemandeur().equals(demandeur.getIdDemandeur()))
                .max((p1, p2) -> p1.getIdPasseport().compareTo(p2.getIdPasseport()))
                .orElseThrow(() -> new RuntimeException("Aucun passeport trouvé pour ce demandeur"));

        // Récupère la dernière demande du demandeur pour récupérer le type de visa
        Demande derniereDemande = demandeRepository.findAll().stream()
                .filter(d -> d.getDemandeur().getIdDemandeur().equals(demandeur.getIdDemandeur()))
                .max((d1, d2) -> d1.getIdDemande().compareTo(d2.getIdDemande()))
                .orElseThrow(() -> new RuntimeException("Aucune demande antérieure trouvée pour ce demandeur"));

        // Récupère le dernier visa
        Visa dernierVisa = visaRepository.findAll().stream()
                .filter(v -> v.getPasseport().getIdPasseport().equals(dernierePasseport.getIdPasseport()))
                .max((v1, v2) -> v1.getIdVisa().compareTo(v2.getIdVisa()))
                .orElse(new Visa());

        // Crée la nouvelle demande Duplicata
        Demande demande = new Demande();
        demande.setDateDemande(new java.sql.Timestamp(System.currentTimeMillis()));
        demande.setTypeDemande(typeDemandeRepository.findById(idTypeDemande)
                .orElseThrow(() -> new RuntimeException("TypeDemande introuvable")));
        demande.setTypeVisa(derniereDemande.getTypeVisa());
        demande.setDemandeur(demandeur);
        demande.setDemandeParent(derniereDemande); // Lien vers la demande parent

        Demande savedDemande = demandeRepository.save(demande);

        // Crée le statut initial
        StatutDemande initialStatus = statutDemandeRepository.findByLibelle("Visa approuvé");
        DemandeStatut demandeStatut = new DemandeStatut();
        demandeStatut.setDemande(savedDemande);
        demandeStatut.setStatutDemande(initialStatus);
        demandeStatut.setDateStatut(Date.valueOf(LocalDate.now()));
        demandeStatutRepository.save(demandeStatut);

        // Crée la carte résident
        CarteResident carteResident = new CarteResident();
        carteResident.setDemande(savedDemande);
        carteResident.setPasseport(dernierePasseport);
        carteResident.setDateDebut(dernierVisa.getDateDebut());
        carteResident.setDateFin(dernierVisa.getDateFin());
        carteResident.setReference(dernierVisa.getReference());
        carteResidentRepository.save(carteResident);

        return savedDemande;
    }

    @Override
    @Transactional
    public void processUploadsForDemande(Integer idDemande, List<MultipartFile> files, List<Integer> idDossiers) {
        if (idDemande == null) {
            throw new IllegalArgumentException("idDemande obligatoire");
        }
        if (idDossiers == null || idDossiers.isEmpty()) {
            throw new IllegalArgumentException("Au moins un dossier doit être sélectionné");
        }

        Demande demande = demandeRepository.findById(idDemande)
                .orElseThrow(() -> new RuntimeException("Demande introuvable: " + idDemande));

        Integer idDemandeur = demande.getDemandeur() != null ? demande.getDemandeur().getIdDemandeur() : null;
        if (idDemandeur == null) {
            throw new RuntimeException("Demandeur introuvable pour la demande: " + idDemande);
        }

        int fileCount = (files == null) ? 0 : files.size();
        System.out.println(" ============================= Starting upload processing for demande " + idDemande + ": fileCount=" + fileCount + ", idDossiers=" + idDossiers + " =============================");
        for (int i = 0; i < idDossiers.size(); i++) {
            Integer idDossier = idDossiers.get(i);
            MultipartFile file = (i < fileCount) ? files.get(i) : null;

            System.out.println(" ====================== " + i + " < " + fileCount + " ======================");
            if (file != null) {
                System.out.println( " ============================= " + file.getName() + " - " + file.getOriginalFilename() + " - " + file.getSize());
            } else {
                System.out.println(" ============================= file is null");
            }

            boolean hasNewUpload = file != null && !file.isEmpty();
            System.out.println(" ============================= Processing dossier " + idDossier + ": hasNewUpload=" + hasNewUpload + " =============================");
            if (hasNewUpload) {
                System.out.println(" ========================= Tafiditra ato ilay boucle =========================");
                pieceJustificativeService.uploadAndSavePieceJustificative(file, idDossier, idDemandeur);
            }
        }

            // Complétude: on ne dépend pas de la liste idDossiers de la requête (qui peut contenir seulement 1 dossier lors d'une modification).
            // On calcule les dossiers requis depuis la base selon le type de visa (et éventuellement le type de demande).
            List<Dossier> requiredDossiers = dossierRepository.findByTypeVisa(demande.getTypeVisa());
            if (demande.getTypeDemande() != null && demande.getTypeDemande().getIdTypeDemande() != null) {
                Integer idTypeDemande = demande.getTypeDemande().getIdTypeDemande();
                requiredDossiers = requiredDossiers.stream()
                    .filter(d -> d.getTypeDemande() == null
                        || (d.getTypeDemande().getIdTypeDemande() != null
                        && d.getTypeDemande().getIdTypeDemande().equals(idTypeDemande)))
                    .toList();
            }

            boolean allPiecesUploaded = !requiredDossiers.isEmpty() && requiredDossiers.stream()
                .allMatch(dossier -> !pieceJustificativeService
                    .findByDemandeurAndDossier(idDemandeur, dossier.getIdDossier())
                    .isEmpty());

        if (!allPiecesUploaded) {
            return;
        }

        StatutDemande scanTermine = statutDemandeRepository.findById(4)
            .orElseThrow(() -> new RuntimeException(
                "StatutDemande id=4 ('Scan terminé') introuvable. Exécuter sql/insert.sql ou corriger la table statut_demande."
            ));

        boolean alreadyMarked = demandeStatutRepository.findByDemande(demande).stream()
                .anyMatch(ds -> ds.getStatutDemande() != null
                        && "Scan terminé".equalsIgnoreCase(ds.getStatutDemande().getLibelle()));
        if (alreadyMarked) {
            return;
        }

        DemandeStatut demandeStatut = new DemandeStatut();
        demandeStatut.setDemande(demande);
        demandeStatut.setStatutDemande(scanTermine);
        demandeStatut.setDateStatut(Date.valueOf(LocalDate.now()));
        demandeStatutRepository.save(demandeStatut);
    }
}