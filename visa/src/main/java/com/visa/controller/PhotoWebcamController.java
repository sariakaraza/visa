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
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/photo")
public class PhotoWebcamController {

    @Autowired
    private DemandeService demandeService; // À adapter selon le nom de votre service Demande

    @Autowired
    private DossierService dossierService;

    @Autowired
    private PieceJustificativeService pieceService; // À adapter (le service qui gère la table piece_justificative)

    // 1. Affiche la page de la webcam
    @GetMapping("/capture/{idDemande}")
    public String showWebcamPage(@PathVariable("idDemande") Integer idDemande, Model model) {
        model.addAttribute("idDemande", idDemande);
        return "demande/capture-photo"; // correspondra à capture-photo.html
    }

    // 2. Traite l'image envoyée en AJAX par le JavaScript
    @PostMapping("/upload/{idDemande}")
    @ResponseBody
    public String handlePhotoUpload(@PathVariable("idDemande") Integer idDemande, 
                                    @RequestBody Map<String, String> json) {
        try {
            // Récupération de la demande et du demandeur
            Demande demande = demandeService.findById(idDemande)
                    .orElseThrow(() -> new RuntimeException("Demande introuvable"));
            Demandeur demandeur = demande.getDemandeur();

            // Extraction de la chaîne Base64 envoyée par JS
            String base64Image = json.get("image");
            if (base64Image == null || !base64Image.contains(",")) {
                return "Erreur : Données de l'image invalides.";
            }
            String rawBase64 = base64Image.split(",")[1];
            byte[] imageBytes = Base64.getDecoder().decode(rawBase64);

            // Préparation du dossier de stockage physique sur le serveur
            String uploadDir = "uploads/photos/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs(); // Crée le dossier s'il n'existe pas
            }

            // Nom du fichier unique
            String fileName = "photo_demandeur_" + demandeur.getIdDemandeur() + "_" + System.currentTimeMillis() + ".jpg";
            String filePath = uploadDir + fileName;

            // Écriture du fichier sur le disque
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(imageBytes);
            }

            // Recherche du dossier "Photo d'identité (Webcam)" adapté au type de visa
            Dossier dossierPhoto = dossierService.findPhotoDossier(demande.getTypeVisa())
                    .orElseThrow(() -> new RuntimeException("Le type de dossier 'Photo' n'est pas configuré."));

            // Enregistrement dans la table piece_justificative
            PieceJustificative piece = new PieceJustificative();
            piece.setCheminFichier(filePath); // Stocke "uploads/photos/le_nom.jpg"
            piece.setDateAjout(Timestamp.valueOf(LocalDateTime.now()));
            piece.setDossier(dossierPhoto);
            piece.setDemandeur(demandeur);

            pieceService.save(piece); // Sauvegarde en BDD

            // Après sauvegarde, vérifier si tous les dossiers requis sont présents
            try {
                demandeService.processUploadsForDemande(idDemande, null, java.util.List.of(dossierPhoto.getIdDossier()));
            } catch (Exception e) {
                // Ne pas empêcher le succès en cas d'erreur secondaire
                e.printStackTrace();
            }

            return "OK";
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de l'enregistrement physique du fichier.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur : " + e.getMessage();
        }
    }
}