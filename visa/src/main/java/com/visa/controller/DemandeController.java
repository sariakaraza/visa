package com.visa.controller;

import com.visa.entity.*;
import com.visa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private DemandeurService demandeurService;

    @Autowired
    private PasseportService passeportService;

    @Autowired
    private VisaTransformableService visaTransformableService;

    @Autowired
    private LieuService lieuService;

    @Autowired
    private TypeDemandeService typeDemandeService;

    @Autowired
    private TypeVisaService typeVisaService;

    @Autowired
    private NationaliteService nationaliteService;

    @Autowired
    private SituationFamilialeService situationFamilialeService;

    @Autowired
    private DossierService dossierService;

    // @GetMapping("/nouvelle-demande")
    // public String showForm(Model model) {
    //     List<TypeDemande> typeDemandes = typeDemandeService.findAll();
    //     List<TypeVisa> typeVisas = typeVisaService.findAll();
    //     List<Nationalite> nationalites = nationaliteService.findAll();
    //     List<SituationFamiliale> situations = situationFamilialeService.findAll();
    //     List<Dossier> dossiers = dossierService.findAll();

    //     model.addAttribute("typeDemandes", typeDemandes);
    //     model.addAttribute("typeVisas", typeVisas);
    //     model.addAttribute("nationalites", nationalites);
    //     model.addAttribute("situations", situations);
    //     model.addAttribute("dossiers", dossiers);

    //     return "nouvelle-demande";
    // }

    @GetMapping("/nouvelle-demande")
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

        return "nouvelle-demande";
    }

    @PostMapping("/nouvelle-demande")
    public String submitForm(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam Date dateNaissance,
            @RequestParam String adresse,
            @RequestParam Integer idNationalite,
            @RequestParam Integer idSituationFamiliale,
            @RequestParam Integer idTypeDemande,
            @RequestParam Integer idTypeVisa,
            @RequestParam String numeroPasseport,
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
        return "redirect:/nouvelle-demande";
    }
}