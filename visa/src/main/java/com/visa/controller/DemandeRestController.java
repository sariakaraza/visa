package com.visa.controller;

import com.visa.entity.Demande;
import com.visa.entity.DemandeStatut;
import com.visa.service.DemandeService;
import com.visa.service.DemandeStatutService;
import com.visa.entity.Visa;
import com.visa.repository.DemandeRepository;
import com.visa.repository.VisaRepository;
import com.visa.service.DemandeStatutService;
import org.springframework.http.HttpStatus;
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
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/demandes")
public class DemandeRestController {

    private final DemandeService demandeService;
    private final DemandeStatutService demandeStatutService;
  
    private final DemandeRepository demandeRepository;
    private final VisaRepository visaRepository;

    public DemandeRestController(
            DemandeRepository demandeRepository,
            VisaRepository visaRepository,
            DemandeService demandeService, 
            DemandeStatutService demandeStatutService
    ) {
        this.demandeRepository = demandeRepository;
        this.visaRepository = visaRepository;
        this.demandeStatutService = demandeStatutService;
        this.demandeService = demandeService;
    }
}

    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestParam(required = false) String numDemande,
                                        @RequestParam(required = false) String numPasseport) {
        if ((numDemande == null || numDemande.trim().isEmpty()) &&
            (numPasseport == null || numPasseport.trim().isEmpty())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Au moins un paramètre numDemande ou numPasseport doit être fourni"));
        }

        Optional<Demande> demandeOpt = Optional.empty();
        
        // D'abord chercher par numéro de demande
        if (numDemande != null && !numDemande.trim().isEmpty()) {
            demandeOpt = demandeService.findByReferenceDemande(numDemande.trim());
        }
        
        // Si pas trouvé, chercher par numéro de passeport
        if (demandeOpt.isEmpty() && numPasseport != null && !numPasseport.trim().isEmpty()) {
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
        response.put("numeroRecherche", numDemande != null && !numDemande.trim().isEmpty() ? numDemande.trim() : numPasseport.trim());
        response.put("typeRecherche", numDemande != null && !numDemande.trim().isEmpty() ? "Demande" : "Passeport");
        response.put("nomDemandeur", demande.getDemandeur().getNom() + " " + (demande.getDemandeur().getPrenom() != null ? demande.getDemandeur().getPrenom() : ""));
        response.put("dateCreation", demande.getDateDemande());
        response.put("statusHistory", statusHistory);
        // response.put("cheminQr", demande.getCheminQr());

        return ResponseEntity.ok(response);
    }
}
    public ResponseEntity<?> history(
            @RequestParam(required = false) String numDemande,
            @RequestParam(required = false) String numPasseport
    ) {
        if ((numDemande == null || numDemande.isBlank()) && (numPasseport == null || numPasseport.isBlank())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Au moins un paramètre est obligatoire: numDemande ou numPasseport"));
        }

        Optional<Demande> demandeOpt = resolveDemande(numDemande, numPasseport);
        if (demandeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Demande introuvable"));
        }

        Demande demande = demandeOpt.get();

        List<DemandeStatut> statuts = demandeStatutService.findByDemande(demande).stream()
                .sorted(Comparator
                        .comparing(DemandeStatut::getDateStatut, Comparator.nullsLast(Date::compareTo))
                        .thenComparing(DemandeStatut::getIdDemandeStatut, Comparator.nullsLast(Integer::compareTo)))
                .toList();

        List<StatutEntry> timeline = statuts.stream().map(ds -> new StatutEntry(
                ds.getDateStatut() != null ? ds.getDateStatut().toLocalDate().toString() : null,
                ds.getStatutDemande() != null ? ds.getStatutDemande().getLibelle() : null,
                null
        )).toList();

        HistoryResponse response = new HistoryResponse(
                demande.getIdDemande(),
                demande.getReferenceDemande(),
                demande.getDemandeur() != null ? (demande.getDemandeur().getNom() + " " + demande.getDemandeur().getPrenom()) : null,
                formatTimestamp(demande.getDateDemande()),
                timeline
        );

        return ResponseEntity.ok(response);
    }

    private Optional<Demande> resolveDemande(String numDemande, String numPasseport) {
        if (numDemande != null && !numDemande.isBlank()) {
            Optional<Demande> byRef = demandeRepository.findByReferenceDemandeIgnoreCase(numDemande.trim());
            if (byRef.isPresent()) {
                return byRef;
            }
            // fallback si le front passe un id numérique
            try {
                Integer id = Integer.parseInt(numDemande.trim());
                return demandeRepository.findById(id);
            } catch (NumberFormatException ignored) {
                // ignore
            }
        }

        if (numPasseport != null && !numPasseport.isBlank()) {
            Optional<Visa> visaOpt = visaRepository
                    .findFirstByPasseport_NumeroIgnoreCaseOrderByDemande_DateDemandeDescIdVisaDesc(numPasseport.trim());
            return visaOpt.map(Visa::getDemande);
        }

        return Optional.empty();
    }

    private String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        LocalDateTime ldt = timestamp.toLocalDateTime();
        return ldt.toString();
    }

    public record HistoryResponse(
            Integer id,
            String numDemande,
            String demandeur,
            String dateCreation,
            List<StatutEntry> timeline
    ) {
    }

    public record StatutEntry(
            String date,
            String statutLibelle,
            String commentaire
    ) {
    }

    public record ErrorResponse(String message) {
    }
}
