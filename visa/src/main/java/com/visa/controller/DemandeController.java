package com.visa.controller;

import com.visa.entity.*;
import com.visa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
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

        Map<Integer, List<Dossier>> dossiersData = new HashMap<>();

        for (TypeDemande td : typeDemandes) {
            List<Dossier> liste = dossiers.stream()
                    .filter(d -> d.getTypeDemande().getIdTypeDemande()
                            .equals(td.getIdTypeDemande()))
                    .toList();

            dossiersData.put(td.getIdTypeDemande(), liste);
        }

        model.addAttribute("typeDemandes", typeDemandes);
        model.addAttribute("typeVisas", typeVisas);
        model.addAttribute("nationalites", nationalites);
        model.addAttribute("situations", situations);

        model.addAttribute("dossiersData", dossiersData);

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

        // Create Demande
        Demande demande = new Demande();
        demande.setDateDemande(new Timestamp(System.currentTimeMillis()));
        demande.setTypeDemande(typeDemandeService.findById(idTypeDemande).orElseThrow());
        demande.setTypeVisa(typeVisaService.findById(idTypeVisa).orElseThrow());
        demande.setDemandeur(savedDemandeur);

        demandeService.save(demande);

        model.addAttribute("message", "Demande créée avec succès!");
        return "redirect:/nouvelle-demande";
    }
}