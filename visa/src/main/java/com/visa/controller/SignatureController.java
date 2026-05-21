package com.visa.controller;

import com.visa.entity.*;
import com.visa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;

@Controller
@RequestMapping("/signature")
public class SignatureController {

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private DossierService dossierService;

    @Autowired
    private PieceJustificativeService pieceService;

    // 1. Afficher la page de dessin de signature
    @GetMapping("/draw/{idDemande}")
    public String showSignaturePage(@PathVariable("idDemande") Integer idDemande, Model model) {
        model.addAttribute("idDemande", idDemande);
        return "demande/draw-signature"; // correspondra à draw-signature.html
    }

    // 2. Traiter l'image PNG transparente envoyée par le canvas
    @PostMapping("/upload/{idDemande}")
    @ResponseBody
    public String handleSignatureUpload(@PathVariable("idDemande") Integer idDemande, 
                                        @RequestBody Map<String, String> json) {
        try {
            Demande demande = demandeService.findById(idDemande)
                    .orElseThrow(() -> new RuntimeException("Demande introuvable"));
            Demandeur demandeur = demande.getDemandeur();

            String base64Image = json.get("image");
            if (base64Image == null || !base64Image.contains(",")) {
                return "Erreur : Données invalides.";
            }
            String rawBase64 = base64Image.split(",")[1];
            byte[] imageBytes = Base64.getDecoder().decode(rawBase64);

            // Dossier de stockage physique
            String uploadDir = "uploads/signatures/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Fichier unique (.png pour garder la transparence du tracé)
            String fileName = "signature_" + demandeur.getIdDemandeur() + "_" + System.currentTimeMillis() + ".png";
            String filePath = uploadDir + fileName;

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(imageBytes);
            }

            // Récupération du dossier "Signature" pour ce type de visa
            Dossier dossierSignature = dossierService.findSignatureDossier(demande.getTypeVisa())
                    .orElseThrow(() -> new RuntimeException("Dossier 'Signature' non configuré."));

            // Enregistrement en BDD
            PieceJustificative piece = new PieceJustificative();
            piece.setDateAjout(Timestamp.valueOf(LocalDateTime.now()));
            piece.setCheminFichier(filePath);
            piece.setDossier(dossierSignature);
            piece.setDemandeur(demandeur);

            pieceService.save(piece);

            // Après sauvegarde, vérifier si tous les dossiers requis sont présents
            try {
                demandeService.processUploadsForDemande(idDemande, null, java.util.List.of(dossierSignature.getIdDossier()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "OK";
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de l'écriture du fichier.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur : " + e.getMessage();
        }
    }
}