package com.visa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "statut_visa")
public class StatutVisa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idStatutVisa;

    private String libelle;

    public StatutVisa() {
    }

    public StatutVisa(String libelle) {
        this.libelle = libelle;
    }

    public Integer getIdStatutVisa() {
        return idStatutVisa;
    }

    public void setIdStatutVisa(Integer idStatutVisa) {
        this.idStatutVisa = idStatutVisa;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
