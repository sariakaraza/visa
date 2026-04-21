package com.visa.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "statut_visa")
@Getter
@Setter
@NoArgsConstructor
public class StatutVisa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idStatutVisa;

    private String libelle;

    public StatutVisa(String libelle) {
        this.libelle = libelle;
    }
}
