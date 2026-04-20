package com.visa.entity;

import java.sql.Date;
import java.sql.Timestamp;
import jakarta.persistence.*;


@Entity
@Table(name = "visa_transformable")
public class VisaTransformable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVisaTransformable;

    private String reference;
    private Date dateEntree;
    private Timestamp dateExpiration;

    @ManyToOne
    @JoinColumn(name = "id_lieu", nullable = false)
    private Lieu lieu;

    @ManyToOne
    @JoinColumn(name = "id_passeport", nullable = false)
    private Passeport passeport;

    public VisaTransformable() {
    }

    public Integer getIdVisaTransformable() {
        return idVisaTransformable;
    }

    public void setIdVisaTransformable(Integer idVisaTransformable) {
        this.idVisaTransformable = idVisaTransformable;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(Date dateEntree) {
        this.dateEntree = dateEntree;
    }

    public Timestamp getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Timestamp dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Lieu getLieu() {
        return lieu;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }

    public Passeport getPasseport() {
        return passeport;
    }

    public void setPasseport(Passeport passeport) {
        this.passeport = passeport;
    }
}
