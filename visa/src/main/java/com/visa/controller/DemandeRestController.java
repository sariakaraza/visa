package com.visa.controller;

import com.visa.entity.Demande;
import com.visa.entity.DemandeStatut;
import com.visa.service.DemandeService;
import com.visa.service.DemandeStatutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/demandes")
public class DemandeRestController {

    private final DemandeService demandeService;
    private final DemandeStatutService demandeStatutService;

    public DemandeRestController(DemandeService demandeService, DemandeStatutService demandeStatutService) {
        this.demandeService = demandeService;
        this.demandeStatutService = demandeStatutService;
    }

    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestParam(required = false) String numDemande,
                                        @RequestParam(required = false) String numPasseport) {
        if ((numDemande == null || numDemande.trim().isEmpty()) &&
            (numPasseport == null || numPasseport.trim().isEmpty())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Au moins un paramètre numDemande ou numPasseport doit être fourni"));
        }

        Optional<Demande> demandeOpt = Optional.empty();
        if (numDemande != null && !numDemande.trim().isEmpty()) {
            demandeOpt = demandeService.findByReferenceDemande(numDemande.trim());
        } else if (numPasseport != null && !numPasseport.trim().isEmpty()) {
            demandeOpt = demandeService.findByPasseportNumero(numPasseport.trim());
        }

        if (demandeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Demande demande = demandeOpt.get();
        List<DemandeStatut> statuts = demandeStatutService.findByDemande(demande);

        List<Map<String, Object>> statusHistory = statuts.stream()
            .sorted((s1, s2) -> s1.getDateStatut().compareTo(s2.getDateStatut()))
            .map(statut -> {
                Map<String, Object> statusMap = new HashMap<>();
                statusMap.put("date", statut.getDateStatut());
                statusMap.put("statutLibelle", statut.getStatutDemande().getLibelle());
                // Assuming commentaire is added later, for now put null or empty
                statusMap.put("commentaire", null);
                return statusMap;
            })
            .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("idDemande", demande.getIdDemande());
        response.put("numeroDemande", demande.getReferenceDemande());
        response.put("nomDemandeur", demande.getDemandeur().getNom() + " " + (demande.getDemandeur().getPrenom() != null ? demande.getDemandeur().getPrenom() : ""));
        response.put("dateCreation", demande.getDateDemande());
        response.put("statusHistory", statusHistory);

        return ResponseEntity.ok(response);
    }
}