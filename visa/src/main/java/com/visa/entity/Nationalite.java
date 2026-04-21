package com.visa.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "nationalite")

@NoArgsConstructor
public class Nationalite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNationalite;

    private String libelle;

    public Nationalite(String libelle) {
        this.libelle = libelle;
    }
}
