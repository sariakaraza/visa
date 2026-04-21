package com.visa.entity;

import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.Getter;
// import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "demande")
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDemande;

    private Timestamp dateDemande;

    @ManyToOne
    @JoinColumn(name = "id_type_demande", nullable = false)
    private TypeDemande typeDemande;

    @ManyToOne
    @JoinColumn(name = "id_type_visa", nullable = false)
    private TypeVisa typeVisa;

    @ManyToOne
    @JoinColumn(name = "id_demandeur", nullable = false)
    private Demandeur demandeur;

    public Demande() {
    }

   
}
