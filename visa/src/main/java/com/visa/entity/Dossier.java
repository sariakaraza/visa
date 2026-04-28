package com.visa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dossier")

public class Dossier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer idDossier;

    private String libelle;

    @ManyToOne
    @JoinColumn(name = "id_type_demande", nullable = false)
    private TypeDemande typeDemande;

    @ManyToOne
    @JoinColumn(name = "id_type_demande")
    private TypeDemande typeDemande;


}
