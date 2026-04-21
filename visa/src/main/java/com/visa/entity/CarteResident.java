package com.visa.entity;

import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;   


@Getter
@Setter
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

}
