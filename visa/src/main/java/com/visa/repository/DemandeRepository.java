package com.visa.repository;

import com.visa.entity.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Integer> {

    Optional<Demande> findByReferenceDemande(String referenceDemande);

    @Query("SELECT d FROM Demande d JOIN d.demandeur dem JOIN dem.passeports p WHERE p.numero = :numero")
    Optional<Demande> findByPasseportNumero(@Param("numero") String numero);
}
