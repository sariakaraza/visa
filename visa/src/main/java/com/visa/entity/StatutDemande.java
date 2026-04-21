package com.visa.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "statut_demande")
@Getter
@Setter
@NoArgsConstructor
public class StatutDemande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idStatutDemande;

    private String libelle;

    public StatutDemande(String libelle) {
        this.libelle = libelle;
    }
}

