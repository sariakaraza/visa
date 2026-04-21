package com.visa.entity;

import java.sql.Date;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


@Entity
@Table(name = "demandeur")
public class Demandeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDemandeur;

    private String nom;
    private String prenom;
    private String nomJeuneFille;
    private Date dateNaissance;
    private String adresse;
    private String telephone;

    @ManyToOne
    @JoinColumn(name = "id_nationalite", nullable = false)
    private Nationalite nationalite;

    @ManyToOne
    @JoinColumn(name = "id_situation_familiale", nullable = false)
    private SituationFamiliale situationFamiliale;

    public Demandeur() {
    }

   
}
