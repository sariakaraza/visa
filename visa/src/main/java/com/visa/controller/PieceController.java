package com.visa.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.visa.entity.PieceJustificative;
import com.visa.service.PieceJustificativeService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class PieceController {
    private final PieceJustificativeService pieceJustificativeService;

    public PieceController(PieceJustificativeService pieceJustificativeService) {
        this.pieceJustificativeService = pieceJustificativeService;
    }

    @GetMapping("/pieces/pdf")
    public void afficherPdf(HttpServletResponse response) throws Exception {

        response.setContentType("application/pdf");

        // IMPORTANT :
        response.setHeader("Content-Disposition", "inline; filename=pieces.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        List<PieceJustificative> pieces = pieceJustificativeService.findAll();

        for (PieceJustificative p : pieces) {

            if (p.getCheminFichier() != null) {

                String chemin = "src/main/resources/static/" + p.getCheminFichier();

                Image image = Image.getInstance(chemin);

                image.scaleToFit(500, 700);

                document.add(new Paragraph(
                    p.getDossier().getLibelle()
                ));

                document.add(image);

                document.newPage();
            }
        }

        document.close();
    }
}
