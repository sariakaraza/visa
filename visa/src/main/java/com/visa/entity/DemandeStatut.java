package com.visa.entity;

import java.sql.Date;
import jakarta.persistence.*;


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

    public Integer getIdDemandeStatut() {
        return idDemandeStatut;
    }

    public void setIdDemandeStatut(Integer idDemandeStatut) {
        this.idDemandeStatut = idDemandeStatut;
    }

    public Date getDateStatut() {
        return dateStatut;
    }

    public void setDateStatut(Date dateStatut) {
        this.dateStatut = dateStatut;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public StatutDemande getStatutDemande() {
        return statutDemande;
    }

    public void setStatutDemande(StatutDemande statutDemande) {
        this.statutDemande = statutDemande;
    }
}
