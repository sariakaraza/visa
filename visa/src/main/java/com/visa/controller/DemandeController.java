package com.visa.controller;

import com.visa.entity.*;
import com.visa.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/demande")
public class DemandeController {

    private final DemandeService demandeService;
    private final DemandeurService demandeurService;
    private final PasseportService passeportService;
    private final VisaTransformableService visaTransformableService;
    private final LieuService lieuService;
    private final TypeDemandeService typeDemandeService;
    private final TypeVisaService typeVisaService;
    private final NationaliteService nationaliteService;
    private final SituationFamilialeService situationFamilialeService;
    private final DossierService dossierService;
    private final PieceJustificativeService pieceJustificativeService;

    public DemandeController(DemandeService demandeService, DemandeurService demandeurService, PasseportService passeportService, VisaTransformableService visaTransformableService, LieuService lieuService, TypeDemandeService typeDemandeService, TypeVisaService typeVisaService, NationaliteService nationaliteService, SituationFamilialeService situationFamilialeService, DossierService dossierService, PieceJustificativeService pieceJustificativeService) {
        this.demandeService = demandeService;
        this.demandeurService = demandeurService;
        this.passeportService = passeportService;
        this.visaTransformableService = visaTransformableService;
        this.lieuService = lieuService;
        this.typeDemandeService = typeDemandeService;
        this.typeVisaService = typeVisaService;
        this.nationaliteService = nationaliteService;
        this.situationFamilialeService = situationFamilialeService;
        this.dossierService = dossierService;
        this.pieceJustificativeService = pieceJustificativeService;
    }

    @GetMapping("/new")
    public String showForm(Model model) {

        List<TypeDemande> typeDemandes = typeDemandeService.findAll();
        List<TypeVisa> typeVisas = typeVisaService.findAll();
        List<Nationalite> nationalites = nationaliteService.findAll();
        List<SituationFamiliale> situations = situationFamilialeService.findAll();
        List<Dossier> dossiers = dossierService.findAll();
        List<Lieu> lieux = lieuService.findAll();

        TypeDemande nouveauTitre = typeDemandes.stream()
            .filter(td -> td.getLibelle() != null && td.getLibelle().equalsIgnoreCase("Nouveau Titre"))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Le type de demande 'Nouveau Titre' est introuvable."));

        Map<Integer, List<Dossier>> dossiersByVisa = new HashMap<>();
        for (TypeVisa typeVisa : typeVisas) {
            List<Dossier> dossiersForVisa = dossiers.stream()
                    .filter(d -> d.getTypeVisa() != null
                            && d.getTypeVisa().getIdTypeVisa().equals(typeVisa.getIdTypeVisa()))
                    .toList();
            dossiersByVisa.put(typeVisa.getIdTypeVisa(), dossiersForVisa);
        }

        model.addAttribute("idTypeDemandeFixed", nouveauTitre.getIdTypeDemande());
        model.addAttribute("typeVisas", typeVisas);
        model.addAttribute("nationalites", nationalites);
        model.addAttribute("situations", situations);
        model.addAttribute("lieux", lieux);
        model.addAttribute("dossiersByVisa", dossiersByVisa);

        return "demande/form";
    }

    @PostMapping("/new")
    public String submitForm(
            @RequestParam(required = true) String nom,
            @RequestParam String prenom,
            @RequestParam(required = true) Date dateNaissance,
            @RequestParam(required = false) String nomJeuneFille,
            @RequestParam(required = false) String telephone,
            @RequestParam(required = true) String adresse,
            @RequestParam(required = true) Integer idNationalite,
            @RequestParam(required = true) Integer idSituationFamiliale,
            @RequestParam(required = true) Integer idTypeDemande,
            @RequestParam(required = true) Integer idTypeVisa,
            @RequestParam(required = true) String numeroPasseport,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDelivrancePasseport,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateExpirationPasseport,
            @RequestParam(required = false) String referenceVisa,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEntreeVisa,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateExpirationVisa,
            @RequestParam(required = false) Integer idLieuVisa,
            @RequestParam(required = false) List<Integer> dossiers,
            Model model) {

        // Create Demandeur
        Demandeur demandeur = new Demandeur();
        demandeur.setNom(nom);
        demandeur.setPrenom(prenom);
        demandeur.setDateNaissance(dateNaissance);
        demandeur.setAdresse(adresse);
        demandeur.setNationalite(nationaliteService.findById(idNationalite).orElseThrow());
        demandeur.setSituationFamiliale(situationFamilialeService.findById(idSituationFamiliale).orElseThrow());
        demandeur.setNomJeuneFille(nomJeuneFille);
        demandeur.setTelephone(telephone);

        Demandeur savedDemandeur = demandeurService.save(demandeur);

        // Create Passeport
        Passeport passeport = new Passeport();
        passeport.setNumero(numeroPasseport);
        passeport.setDateDelivrance(Timestamp.valueOf(dateDelivrancePasseport));
        passeport.setDateExpiration(Timestamp.valueOf(dateExpirationPasseport));
        passeport.setDemandeur(savedDemandeur);
        Passeport savedPasseport = passeportService.save(passeport);

        // Create Demande
        Demande demande = new Demande();
        demande.setDateDemande(new Timestamp(System.currentTimeMillis()));
        demande.setTypeDemande(typeDemandeService.findById(idTypeDemande).orElseThrow());
        demande.setTypeVisa(typeVisaService.findById(idTypeVisa).orElseThrow());
        demande.setDemandeur(savedDemandeur);

        demandeService.save(demande);

        // Create VisaTransformable
        VisaTransformable visa = new VisaTransformable();
        visa.setReference(referenceVisa);
        visa.setDateEntree(Date.valueOf(dateEntreeVisa));
        visa.setDateExpiration(Timestamp.valueOf(dateExpirationVisa));
        visa.setLieu(lieuService.findById(idLieuVisa).orElseThrow());
        visa.setPasseport(savedPasseport);
        visaTransformableService.save(visa);

        // Create PieceJustificative for selected dossiers
        if (dossiers != null) {
            for (Integer idDossier : dossiers) {
                PieceJustificative pj = new PieceJustificative();
                pj.setDossier(dossierService.findById(idDossier).orElseThrow());
                pj.setDemandeur(savedDemandeur);
                pj.setDateAjout(new Timestamp(System.currentTimeMillis()));
                pieceJustificativeService.save(pj);
            }
        }

        model.addAttribute("message", "Demande créée avec succès!");
        return "redirect:/demande/list";
    }

    @PostMapping("/transfert-visa")
    public String submitTransfertVisa(
            @RequestParam(required = true) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = true) Date dateNaissance,
            @RequestParam(required = false) String nomJeuneFille,
            @RequestParam(required = false) String telephone,
            @RequestParam(required = true) String adresse,
            @RequestParam(required = true) Integer idNationalite,
            @RequestParam(required = true) Integer idSituationFamiliale,
            @RequestParam(required = true) Integer idTypeDemande,
            @RequestParam(required = true) Integer idTypeVisa,
            @RequestParam(required = true) String numeroPasseport,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDelivrancePasseport,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateExpirationPasseport,
            @RequestParam(required = false) String referenceVisa,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEntreeVisa,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateExpirationVisa,
            @RequestParam(required = false) Integer idLieuVisa,
            Model model) {

        Demandeur demandeur = new Demandeur();
        demandeur.setNom(nom);
        demandeur.setPrenom(prenom);
        demandeur.setDateNaissance(dateNaissance);
        demandeur.setAdresse(adresse);
        demandeur.setNomJeuneFille(nomJeuneFille);
        demandeur.setTelephone(telephone);

        Nationalite nat = new Nationalite();
        nat.setIdNationalite(idNationalite);
        demandeur.setNationalite(nat);

        SituationFamiliale sit = new SituationFamiliale();
        sit.setIdSituationFamiliale(idSituationFamiliale);
        demandeur.setSituationFamiliale(sit);

        Passeport passeport = new Passeport();
        passeport.setNumero(numeroPasseport);
        passeport.setDateDelivrance(Timestamp.valueOf(dateDelivrancePasseport));
        passeport.setDateExpiration(Timestamp.valueOf(dateExpirationPasseport));

        Visa visa = new Visa();
        visa.setReference(referenceVisa);
        if (dateEntreeVisa != null) {
            visa.setDateDebut(Timestamp.valueOf(dateEntreeVisa.atStartOfDay()));
        }
        if (dateExpirationVisa != null) {
            visa.setDateFin(Timestamp.valueOf(dateExpirationVisa));
        }

        demandeService.createTransfertSansAnterieur(demandeur, passeport, visa, idTypeDemande, idTypeVisa, idLieuVisa);

        model.addAttribute("message", "Transfert créé avec succès!");
        return "redirect:/demande/list";
    }

    @PostMapping("/duplicata")
    public String submitDuplicata(
            @RequestParam(required = true) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = true) Date dateNaissance,
            @RequestParam(required = false) String nomJeuneFille,
            @RequestParam(required = false) String telephone,
            @RequestParam(required = true) String adresse,
            @RequestParam(required = true) Integer idNationalite,
            @RequestParam(required = true) Integer idSituationFamiliale,
            @RequestParam(required = true) Integer idTypeDemande,
            @RequestParam(required = true) Integer idTypeVisa,
            @RequestParam(required = true) String numeroPasseport,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDelivrancePasseport,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateExpirationPasseport,
            @RequestParam(required = false) String referenceVisa,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEntreeVisa,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateExpirationVisa,
            @RequestParam(required = false) Integer idLieuVisa,
            Model model) {

        Demandeur demandeur = new Demandeur();
        demandeur.setNom(nom);
        demandeur.setPrenom(prenom);
        demandeur.setDateNaissance(dateNaissance);
        demandeur.setAdresse(adresse);
        demandeur.setNomJeuneFille(nomJeuneFille);
        demandeur.setTelephone(telephone);

        Nationalite nat = new Nationalite();
        nat.setIdNationalite(idNationalite);
        demandeur.setNationalite(nat);

        SituationFamiliale sit = new SituationFamiliale();
        sit.setIdSituationFamiliale(idSituationFamiliale);
        demandeur.setSituationFamiliale(sit);

        Passeport passeport = new Passeport();
        passeport.setNumero(numeroPasseport);
        passeport.setDateDelivrance(Timestamp.valueOf(dateDelivrancePasseport));
        passeport.setDateExpiration(Timestamp.valueOf(dateExpirationPasseport));

        Visa visa = new Visa();
        visa.setReference(referenceVisa);
        if (dateEntreeVisa != null) {
            visa.setDateDebut(Timestamp.valueOf(dateEntreeVisa.atStartOfDay()));
        }
        if (dateExpirationVisa != null) {
            visa.setDateFin(Timestamp.valueOf(dateExpirationVisa));
        }

        demandeService.createDuplicataSansAnterieur(demandeur, passeport, visa, idTypeDemande, idTypeVisa, idLieuVisa);

        model.addAttribute("message", "Duplicata créé avec succès!");
        return "redirect:/demande/list";
    }

    @GetMapping("/list")
    public String listDemandes(Model model) {
        List<Demande> demandes = demandeService.findAll();
        model.addAttribute("demandes", demandes);
        return "demande/list";
    }

    @GetMapping("/{id}")
    public String viewDemande(@PathVariable Integer id, Model model) {
        Demande demande = demandeService.findById(id).orElseThrow();

        List<PieceJustificative> pieces = pieceJustificativeService.findAll().stream()
                .filter(p -> p.getDemandeur().getIdDemandeur().equals(demande.getDemandeur().getIdDemandeur()))
                .toList();

        Passeport passeport = passeportService.findAll().stream()
                .filter(p -> p.getDemandeur().getIdDemandeur().equals(demande.getDemandeur().getIdDemandeur()))
                .findFirst()
                .orElse(null);

        VisaTransformable visaTransformable = null;
        Lieu lieu = null;
        if (passeport != null) {
            visaTransformable = visaTransformableService.findAll().stream()
                    .filter(v -> v.getPasseport() != null && v.getPasseport().getIdPasseport().equals(passeport.getIdPasseport()))
                    .findFirst()
                    .orElse(null);
            if (visaTransformable != null) {
                lieu = visaTransformable.getLieu();
            }
        }

        model.addAttribute("demande", demande);
        model.addAttribute("pieces", pieces);
        model.addAttribute("passeport", passeport);
        model.addAttribute("visaTransformable", visaTransformable);
        model.addAttribute("lieu", lieu);

        return "demande/view";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Demande demande = demandeService.findById(id).orElseThrow();

        List<PieceJustificative> pieces = pieceJustificativeService.findAll().stream()
            .filter(p -> p.getDemandeur().getIdDemandeur().equals(demande.getDemandeur().getIdDemandeur()))
            .toList();

        List<Integer> selectedDossiers = pieces.stream().map(p -> p.getDossier().getIdDossier()).toList();

        List<TypeDemande> typeDemandes = typeDemandeService.findAll();
        List<TypeVisa> typeVisas = typeVisaService.findAll();
        List<Nationalite> nationalites = nationaliteService.findAll();
        List<SituationFamiliale> situations = situationFamilialeService.findAll();
        List<Dossier> dossiers = dossierService.findAll();
        List<Lieu> lieux = lieuService.findAll();

        TypeDemande nouveauTitre = typeDemandes.stream()
            .filter(td -> td.getLibelle() != null && td.getLibelle().equalsIgnoreCase("Nouveau Titre"))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Le type de demande 'Nouveau Titre' est introuvable."));

        Map<Integer, List<Dossier>> dossiersByVisa = new HashMap<>();
        for (TypeVisa typeVisa : typeVisas) {
            List<Dossier> dossiersForVisa = dossiers.stream()
                    .filter(d -> d.getTypeVisa() != null
                            && d.getTypeVisa().getIdTypeVisa().equals(typeVisa.getIdTypeVisa()))
                    .toList();
            dossiersByVisa.put(typeVisa.getIdTypeVisa(), dossiersForVisa);
        }

        Passeport passeport = passeportService.findAll().stream()
            .filter(p -> p.getDemandeur().getIdDemandeur().equals(demande.getDemandeur().getIdDemandeur()))
            .findFirst()
            .orElse(null);

        VisaTransformable visaTransformable = visaTransformableService.findAll().stream()
            .filter(v -> v.getPasseport().getIdPasseport().equals(passeport.getIdPasseport()))
            .findFirst()
            .orElse(null);

        Lieu lieu = visaTransformable.getLieu();

        model.addAttribute("demande", demande);
        model.addAttribute("passeport", passeport);
        model.addAttribute("lieu", lieu);
        model.addAttribute("visaTransformable", visaTransformable);
        model.addAttribute("selectedDossiers", selectedDossiers);
        model.addAttribute("idTypeDemandeFixed", nouveauTitre.getIdTypeDemande());
        model.addAttribute("typeVisas", typeVisas);
        model.addAttribute("nationalites", nationalites);
        model.addAttribute("situations", situations);
        model.addAttribute("lieux", lieux);
        model.addAttribute("dossiersByVisa", dossiersByVisa);

        return "demande/edit";
    }

    @PostMapping("/edit")
    public String updateForm(@RequestParam Integer id, @RequestParam(required = false) List<Integer> dossiers, Model model) {
        Demande demande = demandeService.findById(id).orElseThrow();
        Demandeur demandeur = demande.getDemandeur();

        List<Integer> existing = pieceJustificativeService.findAll().stream()
            .filter(p -> p.getDemandeur().getIdDemandeur().equals(demandeur.getIdDemandeur()))
            .map(p -> p.getDossier().getIdDossier())
            .toList();

        if (dossiers != null) {
            for (Integer dossierId : dossiers) {
                if (!existing.contains(dossierId)) {
                    PieceJustificative pj = new PieceJustificative();
                    pj.setDateAjout(new Timestamp(System.currentTimeMillis()));
                    pj.setDossier(dossierService.findById(dossierId).orElseThrow());
                    pj.setDemandeur(demandeur);
                    pieceJustificativeService.save(pj);
                }
            }
        }

        return "redirect:/demande/list";
    }

    
}