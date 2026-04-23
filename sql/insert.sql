-- =========================
-- situation_familiale
-- =========================
INSERT INTO situation_familiale (libelle) VALUES
('Célibataire'),
('Marié(e)'),
('Divorcé(e)'),
('Veuf / Veuve'),
('Séparé(e)'),
('Union libre');

-- =========================
-- nationalite
-- =========================
INSERT INTO nationalite (libelle) VALUES
('Malagasy');

-- =========================
-- lieu
-- =========================
INSERT INTO lieu (libelle) VALUES
('Antsirabe'),
('Antsiranana'),
('Toamasina'),
('Antananarivo'),
('Fianarantsoa'),
('Mahajanga'),
('Toliara');
-- =========================
-- type_visa
-- =========================
INSERT INTO type_visa (libelle) VALUES
('Investisseur'),
('Travailleur');

-- =========================
-- statut_demande
-- =========================
INSERT INTO statut_demande (libelle) VALUES
('Demande créée'),
('Dossier en cours de traitement'),
('Pièces complémentaires demandées'),
('Visa approuvé'),
('Visa refusé'),
('Demande annulée');

-- =========================
-- type_demande
-- =========================
INSERT INTO type_demande (libelle) VALUES
('Nouveau Titre'),
('Duplicata(transfert de visa)'),
('Duplicata(carte de resident)');

-- =========================
-- Dossiers Investisseur
-- =========================

INSERT INTO dossier (libelle, id_type_demande) VALUES
('Statut de la Société', 1),
('Extrait d’inscription au registre de commerce', 1),
('Carte fiscale', 1);

-- =========================
-- Dossiers Travailleur
-- =========================

INSERT INTO dossier (libelle, id_type_demande) VALUES
('Autorisation emploi délivrée à Madagascar par le Ministère de la Fonction publique', 2),
('Attestation d’emploi délivrée par l’employeur (Original)', 2);