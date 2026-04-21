package com.visa.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "visa_statut")
@Getter
@Setter
@NoArgsConstructor
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
}
