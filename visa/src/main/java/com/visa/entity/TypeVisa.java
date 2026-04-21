package com.visa.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "type_visa")
@Getter
@Setter
@NoArgsConstructor
public class TypeVisa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTypeVisa;

    private String libelle;

    public TypeVisa(String libelle) {
        this.libelle = libelle;
    }
}
