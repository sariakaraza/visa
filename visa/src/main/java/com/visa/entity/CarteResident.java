package com.visa.entity;

import java.sql.Timestamp;
import jakarta.persistence.*;


@Entity
@Table(name = "carte_resident")
public class CarteResident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCarteResident;

    private Timestamp dateDebut;
    private Timestamp dateFin;
    private String reference;

    @ManyToOne
    @JoinColumn(name = "id_demande", nullable = false)
    private Demande demande;

    @ManyToOne
    @JoinColumn(name = "id_passeport", nullable = false)
    private Passeport passeport;

    public CarteResident() {
    }

    public Integer getIdCarteResident() {
        return idCarteResident;
    }

    public void setIdCarteResident(Integer idCarteResident) {
        this.idCarteResident = idCarteResident;
    }

    public Timestamp getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Timestamp dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Timestamp getDateFin() {
        return dateFin;
    }

    public void setDateFin(Timestamp dateFin) {
        this.dateFin = dateFin;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public Passeport getPasseport() {
        return passeport;
    }

    public void setPasseport(Passeport passeport) {
        this.passeport = passeport;
    }
}
