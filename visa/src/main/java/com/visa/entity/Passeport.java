package com.visa.entity;

import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "passeport")
public class Passeport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPasseport;

    private String numero;
    private Timestamp dateDelivrance;
    private Timestamp dateExpiration;

    @ManyToOne
    @JoinColumn(name = "id_demandeur", nullable = false)
    private Demandeur demandeur;

    public Passeport() {
    }

}
