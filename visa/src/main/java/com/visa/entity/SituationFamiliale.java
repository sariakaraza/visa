package com.visa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "situation_familiale")
public class SituationFamiliale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSituationFamiliale;

    private String libelle;

    public SituationFamiliale() {
    }

    public SituationFamiliale(String libelle) {
        this.libelle = libelle;
    }

    public Integer getIdSituationFamiliale() {
        return idSituationFamiliale;
    }

    public void setIdSituationFamiliale(Integer idSituationFamiliale) {
        this.idSituationFamiliale = idSituationFamiliale;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}