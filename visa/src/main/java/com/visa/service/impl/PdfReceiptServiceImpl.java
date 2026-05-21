package com.visa.service.impl;

import com.visa.entity.Demande;
import com.visa.entity.Demandeur;
import com.visa.entity.PieceJustificative;
import com.visa.repository.PieceJustificativeRepository;
import com.visa.service.PdfReceiptService;
import org.springframework.stereotype.Service;

import java.awt.Color;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

@Service
public class PdfReceiptServiceImpl implements PdfReceiptService {

    private final PieceJustificativeRepository pieceRepo;

    public PdfReceiptServiceImpl(PieceJustificativeRepository pieceRepo) {
        this.pieceRepo = pieceRepo;
    }

    @Override
    public byte[] generateReceipt(Demande demande) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.WHITE);
        Font sectionFont = new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE);
        Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.DARK_GRAY);

        PdfPTable titleTable = new PdfPTable(1);
        titleTable.setWidthPercentage(100f);
        PdfPCell titleCell = new PdfPCell(new Phrase("Accusé de réception", titleFont));
        titleCell.setBackgroundColor(new Color(34, 64, 104));
        titleCell.setBorder(PdfPCell.NO_BORDER);
        titleCell.setPadding(14f);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleTable.addCell(titleCell);
        document.add(titleTable);
        document.add(new Paragraph(" "));

        // Petit paragraphe professionnel confirmant la bonne réception des dossiers
        Paragraph confirmation = new Paragraph("Nous accusons réception de l'ensemble des dossiers et pièces justificatives fournis. "
            + "Les documents ont été enregistrés et seront traités conformément aux procédures en vigueur.", normalFont);
        confirmation.setAlignment(Element.ALIGN_JUSTIFIED);
        confirmation.setSpacingAfter(8f);
        document.add(confirmation);

        List<PieceJustificative> pieces = demande.getDemandeur() != null
                ? pieceRepo.findByDemandeur_IdDemandeurOrderByDateAjoutAsc(demande.getDemandeur().getIdDemandeur())
                : List.of();

        PieceJustificative photoPiece = pieces.stream()
                .filter(this::isDemandeurPhoto)
                .findFirst()
                .orElse(null);

        Path qrPath = resolveStoredPath(demande.getCheminQr());
        Path photoPath = photoPiece != null ? resolveStoredPath(photoPiece.getCheminFichier()) : null;

        PdfPTable imageTopTable = new PdfPTable(2);
        imageTopTable.setWidthPercentage(100f);
        imageTopTable.setWidths(new float[]{1.15f, 0.85f});
        imageTopTable.addCell(createImageCell("Photo du demandeur", photoPath, 240, 260));
        imageTopTable.addCell(createImageCell("QR code de la demande", qrPath, 180, 180));
        document.add(imageTopTable);
        document.add(new Paragraph(" "));

        addDemandeurSection(document, demande, sectionFont, normalFont);
        document.add(new Paragraph(" "));

        addPiecesSection(document, pieces, sectionFont, normalFont);

        document.close();

        return baos.toByteArray();
    }

    private void addDemandeurSection(Document document, Demande demande, Font sectionFont, Font normalFont) throws Exception {
        PdfPTable sectionTitle = new PdfPTable(1);
        sectionTitle.setWidthPercentage(100f);
        PdfPCell sectionCell = new PdfPCell(new Phrase("Informations du demandeur", sectionFont));
        sectionCell.setBackgroundColor(new Color(56, 87, 122));
        sectionCell.setBorder(PdfPCell.NO_BORDER);
        sectionCell.setPadding(10f);
        sectionTitle.addCell(sectionCell);
        document.add(sectionTitle);

        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100f);
        infoTable.setWidths(new float[]{1.2f, 2.8f});

        Demandeur demandeur = demande.getDemandeur();
        infoTable.addCell(createLabelCell("Nom", sectionFont));
        infoTable.addCell(createValueCell(demandeur != null ? safeText(demandeur.getNom()) : "-", normalFont));
        infoTable.addCell(createLabelCell("Prénom", sectionFont));
        infoTable.addCell(createValueCell(demandeur != null ? safeText(demandeur.getPrenom()) : "-", normalFont));
        infoTable.addCell(createLabelCell("Nom jeune fille", sectionFont));
        infoTable.addCell(createValueCell(demandeur != null ? safeText(demandeur.getNomJeuneFille()) : "-", normalFont));
        infoTable.addCell(createLabelCell("Date de naissance", sectionFont));
        infoTable.addCell(createValueCell(demandeur != null && demandeur.getDateNaissance() != null ? demandeur.getDateNaissance().toString() : "-", normalFont));
        infoTable.addCell(createLabelCell("Adresse", sectionFont));
        infoTable.addCell(createValueCell(demandeur != null ? safeText(demandeur.getAdresse()) : "-", normalFont));
        infoTable.addCell(createLabelCell("Téléphone", sectionFont));
        infoTable.addCell(createValueCell(demandeur != null ? safeText(demandeur.getTelephone()) : "-", normalFont));
        infoTable.addCell(createLabelCell("Nationalité", sectionFont));
        infoTable.addCell(createValueCell(demandeur != null && demandeur.getNationalite() != null ? safeText(demandeur.getNationalite().getLibelle()) : "-", normalFont));
        infoTable.addCell(createLabelCell("Situation familiale", sectionFont));
        infoTable.addCell(createValueCell(demandeur != null && demandeur.getSituationFamiliale() != null ? safeText(demandeur.getSituationFamiliale().getLibelle()) : "-", normalFont));

        document.add(infoTable);
    }

    private void addPiecesSection(Document document, List<PieceJustificative> pieces, Font sectionFont, Font normalFont) throws Exception {
        PdfPTable sectionTitle = new PdfPTable(1);
        sectionTitle.setWidthPercentage(100f);
        PdfPCell sectionCell = new PdfPCell(new Phrase("Dossiers reçus", sectionFont));
        sectionCell.setBackgroundColor(new Color(56, 87, 122));
        sectionCell.setBorder(PdfPCell.NO_BORDER);
        sectionCell.setPadding(10f);
        sectionTitle.addCell(sectionCell);
        document.add(sectionTitle);

        if (pieces == null || pieces.isEmpty()) {
            Paragraph empty = new Paragraph("Aucun dossier reçu pour le moment.", normalFont);
            empty.setSpacingBefore(6f);
            document.add(empty);
            return;
        }

        PdfPTable piecesTable = new PdfPTable(3);
        piecesTable.setWidthPercentage(100f);
        piecesTable.setWidths(new float[]{2.2f, 1.1f, 2.0f});

        piecesTable.addCell(createHeaderCell("Dossier", sectionFont));
        piecesTable.addCell(createHeaderCell("Date ajout", sectionFont));
        piecesTable.addCell(createHeaderCell("Aperçu", sectionFont));

        for (PieceJustificative piece : pieces) {
            String dossier = piece != null && piece.getDossier() != null ? safeText(piece.getDossier().getLibelle()) : "-";
            String date = piece != null && piece.getDateAjout() != null ? piece.getDateAjout().toString() : "-";
            Path piecePath = piece != null ? resolveStoredPath(piece.getCheminFichier()) : null;
            piecesTable.addCell(createValueCell(dossier, normalFont));
            piecesTable.addCell(createValueCell(date, normalFont));
            piecesTable.addCell(createThumbnailCell(piecePath, 110, 110));
        }

        document.add(piecesTable);
    }

    private PdfPCell createImageCell(String title, Path path, float maxWidth, float maxHeight) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPadding(8f);

        cell.addElement(new Paragraph(title));

        if (path != null && Files.exists(path)) {
            try {
                Image image = Image.getInstance(path.toAbsolutePath().toString());
                image.scaleToFit(maxWidth, maxHeight);
                image.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(image);
                return cell;
            } catch (Exception ignored) {
                // fall through to placeholder text
            }
        }

        cell.addElement(new Paragraph("Indisponible"));
        return cell;
    }

    private PdfPCell createThumbnailCell(Path path, float maxWidth, float maxHeight) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(6f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        if (path != null && Files.exists(path)) {
            try {
                Image image = Image.getInstance(path.toAbsolutePath().toString());
                image.scaleToFit(maxWidth, maxHeight);
                image.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(image);
                return cell;
            } catch (Exception ignored) {
                // fall through to placeholder text
            }
        }

        cell.addElement(new Paragraph("Indisponible"));
        return cell;
    }

    private PdfPCell createHeaderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new Color(34, 64, 104));
        cell.setPadding(8f);
        return cell;
    }

    private PdfPCell createLabelCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new Color(34, 64, 104));
        cell.setPadding(8f);
        return cell;
    }

    private PdfPCell createValueCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(8f);
        return cell;
    }

    private Path resolveStoredPath(String storedPath) {
        if (storedPath == null || storedPath.isBlank()) {
            return null;
        }

        Path path = Path.of(storedPath);
        if (path.isAbsolute()) {
            return path.normalize();
        }

        return Path.of(System.getProperty("user.dir")).resolve(path).normalize();
    }

    private String safeText(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private boolean isDemandeurPhoto(PieceJustificative piece) {
        if (piece == null || piece.getDossier() == null || piece.getDossier().getLibelle() == null) {
            return false;
        }

        String libelle = piece.getDossier().getLibelle().toLowerCase(Locale.ROOT);
        return libelle.contains("photo") && libelle.contains("webcam");
    }
}
