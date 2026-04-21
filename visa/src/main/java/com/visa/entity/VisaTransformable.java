package com.visa.entity;

import java.sql.Date;
import java.sql.Timestamp;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;   


@Getter
@Setter
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
}
