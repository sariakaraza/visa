package com.visa.service.impl;

import com.visa.entity.CarteResident;
import com.visa.entity.Demande;
import com.visa.entity.DemandeStatut;
import com.visa.entity.Demandeur;
import com.visa.entity.Passeport;
import com.visa.entity.StatutDemande;
import com.visa.entity.Visa;
import com.visa.entity.Lieu;
import com.visa.entity.Nationalite;
import com.visa.entity.SituationFamiliale;
import com.visa.repository.CarteResidentRepository;
import com.visa.repository.DemandeRepository;
import com.visa.repository.DemandeStatutRepository;
import com.visa.repository.DemandeurRepository;
import com.visa.repository.PasseportRepository;
import com.visa.repository.StatutDemandeRepository;
import com.visa.repository.TypeDemandeRepository;
import com.visa.repository.TypeVisaRepository;
import com.visa.repository.VisaRepository;
import com.visa.repository.LieuRepository;
import com.visa.repository.NationaliteRepository;
import com.visa.repository.SituationFamilialeRepository;
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

    private final DemandeurRepository demandeurRepository;
    private final PasseportRepository passeportRepository;
    private final VisaRepository visaRepository;
    private final LieuRepository lieuRepository;
    private final TypeDemandeRepository typeDemandeRepository;
    private final TypeVisaRepository typeVisaRepository;
    private final NationaliteRepository nationaliteRepository;
    private final SituationFamilialeRepository situationFamilialeRepository;
    private final CarteResidentRepository carteResidentRepository;

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
            CarteResidentRepository carteResidentRepository

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
}