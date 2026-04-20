package com.visa.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "lieu")
public class Lieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLieu;

    private String libelle;

    public Lieu() {
    }

    public Lieu(String libelle) {
        this.libelle = libelle;
    }

    public Integer getIdLieu() {
        return idLieu;
    }

    public void setIdLieu(Integer idLieu) {
        this.idLieu = idLieu;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
