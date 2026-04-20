package com.visa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "type_demande")
public class TypeDemande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTypeDemande;

    private String libelle;

    public TypeDemande() {
    }

    public TypeDemande(String libelle) {
        this.libelle = libelle;
    }

    public Integer getIdTypeDemande() {
        return idTypeDemande;
    }

    public void setIdTypeDemande(Integer idTypeDemande) {
        this.idTypeDemande = idTypeDemande;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
