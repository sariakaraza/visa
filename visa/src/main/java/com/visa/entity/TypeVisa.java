package com.visa.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "type_visa")
public class TypeVisa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTypeVisa;

    private String libelle;

    public TypeVisa() {
    }

    public TypeVisa(String libelle) {
        this.libelle = libelle;
    }

    public Integer getIdTypeVisa() {
        return idTypeVisa;
    }

    public void setIdTypeVisa(Integer idTypeVisa) {
        this.idTypeVisa = idTypeVisa;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
