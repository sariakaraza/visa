package com.visa.service;

import com.visa.entity.Demande;

public interface PdfReceiptService {
    /**
     * Génère le PDF de l'accusé de réception pour la demande et retourne les octets.
     */
    byte[] generateReceipt(Demande demande) throws Exception;
}
