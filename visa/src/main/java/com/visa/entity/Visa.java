package com.visa.entity;

import java.sql.Timestamp;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;   


@Getter
@Setter
@Entity
@Table(name = "visa")
public class Visa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVisa;

    private Timestamp dateDebut;
    private Timestamp dateFin;
    private String reference;

    @ManyToOne
    @JoinColumn(name = "id_demande", nullable = false)
    private Demande demande;

    @ManyToOne
    @JoinColumn(name = "id_lieu", nullable = false)
    private Lieu lieu;

    @ManyToOne
    @JoinColumn(name = "id_passeport", nullable = false)
    private Passeport passeport;

    public Visa() {
    }

}
