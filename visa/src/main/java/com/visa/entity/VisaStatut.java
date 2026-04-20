package com.visa.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "visa_statut")
public class VisaStatut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVisaStatut;

    private String dateStatut;

    @ManyToOne
    @JoinColumn(name = "id_visa", nullable = false)
    private Visa visa;

    @ManyToOne
    @JoinColumn(name = "id_statut_visa", nullable = false)
    private StatutVisa statutVisa;

    public VisaStatut() {
    }

    public Integer getIdVisaStatut() {
        return idVisaStatut;
    }

    public void setIdVisaStatut(Integer idVisaStatut) {
        this.idVisaStatut = idVisaStatut;
    }

    public String getDateStatut() {
        return dateStatut;
    }

    public void setDateStatut(String dateStatut) {
        this.dateStatut = dateStatut;
    }

    public Visa getVisa() {
        return visa;
    }

    public void setVisa(Visa visa) {
        this.visa = visa;
    }

    public StatutVisa getStatutVisa() {
        return statutVisa;
    }

    public void setStatutVisa(StatutVisa statutVisa) {
        this.statutVisa = statutVisa;
    }
}
