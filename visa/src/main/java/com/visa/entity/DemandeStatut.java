package com.visa.entity;

import java.sql.Date;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "demande_statut")
public class DemandeStatut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDemandeStatut;

    private Date dateStatut;

    @ManyToOne
    @JoinColumn(name = "id_demande", nullable = false)
    private Demande demande;

    @ManyToOne
    @JoinColumn(name = "id_statut_demande", nullable = false)
    private StatutDemande statutDemande;

    public DemandeStatut() {
    }

   
}
