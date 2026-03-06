package com.ynov.adventures.repository;

import com.ynov.adventures.domain.Aventurier;
import com.ynov.adventures.domain.Classe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AventurierRepository extends JpaRepository<Aventurier, UUID> {

    /**
     * Recherche les aventuriers filtrés par classe et niveau
     */
    Page<Aventurier> findByClasseAndNiveauBetween(
            Classe classe,
            Integer niveauMin,
            Integer niveauMax,
            Pageable pageable
    );

    /**
     * Recherche les aventuriers par classe uniquement
     */
    Page<Aventurier> findByClasse(Classe classe, Pageable pageable);

    /**
     * Recherche les aventuriers par niveau
     */
    Page<Aventurier> findByNiveauBetween(Integer niveauMin, Integer niveauMax, Pageable pageable);
}
