package com.visa.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "type_demande")
@Getter
@Setter
@NoArgsConstructor
public class TypeDemande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTypeDemande;

    private String libelle;

    public TypeDemande(String libelle) {
        this.libelle = libelle;
    }
}
