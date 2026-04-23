package com.visa.controller;

import com.visa.entity.*;
import com.visa.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
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

    public DemandeController(DemandeService demandeService, DemandeurService demandeurService, PasseportService passeportService, VisaTransformableService visaTransformableService, LieuService lieuService, TypeDemandeService typeDemandeService, TypeVisaService typeVisaService, NationaliteService nationaliteService, SituationFamilialeService situationFamilialeService, DossierService dossierService) {
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


        model.addAttribute("message", "Demande créée avec succès!");
        return "redirect:/demande/new";
    }

    @GetMapping("/list")
    public String listDemandes(Model model) {
        List<Demande> demandes = demandeService.findAll();
        model.addAttribute("demandes", demandes);
        return "demande/list";
    }
}