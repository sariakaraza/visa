CREATE TABLE situation_familiale(
   id_situation_familiale SERIAL,
   libelle VARCHAR(50) ,
   PRIMARY KEY(id_situation_familiale)
);

CREATE TABLE nationalite(
   id_nationalite SERIAL,
   libelle VARCHAR(50) ,
   PRIMARY KEY(id_nationalite)
);

CREATE TABLE type_visa(
   id_type_visa SERIAL,
   libelle VARCHAR(50) ,
   PRIMARY KEY(id_type_visa)
);

CREATE TABLE lieu(
   id_lieu SERIAL,
   libelle VARCHAR(50) ,
   PRIMARY KEY(id_lieu)
);

CREATE TABLE type_demande(
   id_type_demande SERIAL,
   libelle VARCHAR(50) ,
   PRIMARY KEY(id_type_demande)
);

CREATE TABLE statut_demande(
   id_statut_demande SERIAL,
   libelle VARCHAR(50) ,
   PRIMARY KEY(id_statut_demande)
);

CREATE TABLE statut_visa(
   id_statut_visa SERIAL,
   libelle VARCHAR(50) ,
   PRIMARY KEY(id_statut_visa)
);

CREATE TABLE demandeur(
   id_demandeur SERIAL,
   nom VARCHAR(50) ,
   prenom VARCHAR(50) ,
   nom_jeune_fille VARCHAR(50) ,
   date_naissance DATE,
   adresse VARCHAR(150) ,
   telephone VARCHAR(50) ,
   id_nationalite INTEGER NOT NULL,
   id_situation_familiale INTEGER NOT NULL,
   PRIMARY KEY(id_demandeur),
   FOREIGN KEY(id_nationalite) REFERENCES nationalite(id_nationalite),
   FOREIGN KEY(id_situation_familiale) REFERENCES situation_familiale(id_situation_familiale)
);

CREATE TABLE passeport(
   id_passeport SERIAL,
   numero VARCHAR(50) ,
   date_delivrance TIMESTAMP,
   date_expiration TIMESTAMP,
   id_demandeur INTEGER NOT NULL,
   PRIMARY KEY(id_passeport),
   FOREIGN KEY(id_demandeur) REFERENCES demandeur(id_demandeur)
);

CREATE TABLE demande(
   id_demande SERIAL,
   date_demande TIMESTAMP,
   id_type_demande INTEGER NOT NULL,
   id_type_visa INTEGER NOT NULL,
   id_demandeur INTEGER NOT NULL,
   chemin_qr VARCHAR(255),
   PRIMARY KEY(id_demande),
   FOREIGN KEY(id_type_demande) REFERENCES type_demande(id_type_demande),
   FOREIGN KEY(id_type_visa) REFERENCES type_visa(id_type_visa),
   FOREIGN KEY(id_demandeur) REFERENCES demandeur(id_demandeur)
);

-- Sprint 4 (si la table existe déjà)
-- ALTER TABLE demande ADD COLUMN IF NOT EXISTS chemin_qr VARCHAR(255);

CREATE TABLE demande_statut(
   id_demande_statut SERIAL,
   date_statut DATE,
   id_demande INTEGER NOT NULL,
   id_statut_demande INTEGER NOT NULL,
   PRIMARY KEY(id_demande_statut),
   FOREIGN KEY(id_demande) REFERENCES demande(id_demande),
   FOREIGN KEY(id_statut_demande) REFERENCES statut_demande(id_statut_demande)
);

CREATE TABLE visa_transformable(
   id_visa_transformable SERIAL,
   reference VARCHAR(50) ,
   date_entree DATE,
   date_expiration TIMESTAMP,
   id_lieu INTEGER NOT NULL,
   id_passeport INTEGER NOT NULL,
   PRIMARY KEY(id_visa_transformable),
   FOREIGN KEY(id_lieu) REFERENCES lieu(id_lieu),
   FOREIGN KEY(id_passeport) REFERENCES passeport(id_passeport)
);

CREATE TABLE carte_resident(
   id_carte_resident SERIAL,
   date_debut TIMESTAMP,
   date_fin TIMESTAMP,
   reference VARCHAR(50) ,
   id_demande INTEGER NOT NULL,
   id_passeport INTEGER NOT NULL,
   PRIMARY KEY(id_carte_resident),
   FOREIGN KEY(id_demande) REFERENCES demande(id_demande),
   FOREIGN KEY(id_passeport) REFERENCES passeport(id_passeport)
);

CREATE TABLE visa(
   id_visa SERIAL,
   date_debut TIMESTAMP,
   date_fin TIMESTAMP,
   reference VARCHAR(50) ,
   id_demande INTEGER NOT NULL,
   id_lieu INTEGER NOT NULL,
   id_passeport INTEGER NOT NULL,
   PRIMARY KEY(id_visa),
   FOREIGN KEY(id_demande) REFERENCES demande(id_demande),
   FOREIGN KEY(id_lieu) REFERENCES lieu(id_lieu),
   FOREIGN KEY(id_passeport) REFERENCES passeport(id_passeport)
);

CREATE TABLE visa_statut(
   id_visa_statut SERIAL,
   date_statut VARCHAR(50) ,
   id_visa INTEGER NOT NULL,
   id_statut_visa INTEGER NOT NULL,
   PRIMARY KEY(id_visa_statut),
   FOREIGN KEY(id_visa) REFERENCES visa(id_visa),
   FOREIGN KEY(id_statut_visa) REFERENCES statut_visa(id_statut_visa)
);

CREATE TABLE dossier(
   id_dossier SERIAL,
   libelle VARCHAR(250) ,
   id_type_visa INTEGER NOT NULL,
   PRIMARY KEY(id_dossier),
   FOREIGN KEY(id_type_visa) REFERENCES type_visa(id_type_visa)
);

CREATE TABLE piece_justificative(
   id_piece_justificative SERIAL,
   date_ajout TIMESTAMP,
   chemin_fichier VARCHAR(255),
   id_dossier INTEGER NOT NULL,
   id_demandeur INTEGER NOT NULL,
   PRIMARY KEY(id_piece_justificative),
   FOREIGN KEY(id_dossier) REFERENCES dossier(id_dossier),
   FOREIGN KEY(id_demandeur) REFERENCES demandeur(id_demandeur)
);