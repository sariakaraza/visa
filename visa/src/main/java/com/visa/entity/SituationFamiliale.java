package com.visa.entity;

import jakarta.persistence.*;


import lombok.Getter;
import lombok.Setter;   


@Getter
@Setter
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


}