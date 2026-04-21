package com.visa.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lieu")

public class Lieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLieu;

    private String libelle;

    public Lieu(String libelle) {
        this.libelle = libelle;
    }
}
