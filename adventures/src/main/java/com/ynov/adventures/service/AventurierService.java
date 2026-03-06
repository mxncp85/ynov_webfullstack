package com.ynov.adventures.service;

import com.ynov.adventures.domain.Aventurier;
import com.ynov.adventures.domain.Caracteristiques;
import com.ynov.adventures.domain.Classe;
import com.ynov.adventures.dto.*;
import com.ynov.adventures.exception.AventurierBusinessException;
import com.ynov.adventures.exception.AventurierNotFoundException;
import com.ynov.adventures.repository.AventurierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AventurierService {

    private final AventurierRepository aventurierRepository;
    private final MapperService mapperService;

    /**
     * Récupère la liste paginée des aventuriers avec filtres optionnels
     */
    public AventurierListResponseDTO listAventuriers(
            Integer page,
            Integer limit,
            Classe classe,
            Integer niveauMin,
            Integer niveauMax
    ) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Aventurier> result;

        if (classe != null && niveauMin != null && niveauMax != null) {
            result = aventurierRepository.findByClasseAndNiveauBetween(classe, niveauMin, niveauMax, pageable);
        } else if (classe != null) {
            result = aventurierRepository.findByClasse(classe, pageable);
        } else if (niveauMin != null && niveauMax != null) {
            result = aventurierRepository.findByNiveauBetween(niveauMin, niveauMax, pageable);
        } else {
            result = aventurierRepository.findAll(pageable);
        }

        return AventurierListResponseDTO.builder()
                .data(result.getContent().stream()
                        .map(mapperService::toDTO)
                        .collect(Collectors.toList()))
                .pagination(PaginationDTO.builder()
                        .page(page)
                        .limit(limit)
                        .total((int) result.getTotalElements())
                        .totalPages(result.getTotalPages())
                        .build())
                .build();
    }

    /**
     * Crée un nouvel aventurier
     * Règle métier : Le niveau initial doit être 1
     */
    public AventurierDTO createAventurier(AventurierCreateDTO dto) {
        Aventurier aventurier = Aventurier.builder()
                .nom(dto.getNom())
                .description(dto.getDescription())
                .caracteristiques(mapperService.toCaracteristiques(dto.getCaracteristiques()))
                .niveau(1) // Force le niveau à 1
                .classe(dto.getClasse())
                .build();

        aventurier = aventurierRepository.save(aventurier);
        return mapperService.toDTO(aventurier);
    }

    /**
     * Récupère un aventurier par son ID
     */
    @Transactional(readOnly = true)
    public AventurierDTO getAventurierById(UUID id) {
        Aventurier aventurier = aventurierRepository.findById(id)
                .orElseThrow(() -> new AventurierNotFoundException("Aventurier non trouvé avec l'ID : " + id));
        return mapperService.toDTO(aventurier);
    }

    /**
     * Met à jour complètement un aventurier (PUT)
     * Règles métier appliquées :
     * - Le niveau ne peut jamais descendre
     * - Le niveau ne peut pas monter de plus de 1 à la fois
     */
    public AventurierDTO updateAventurier(UUID id, AventurierUpdateDTO dto) {
        Aventurier aventurier = aventurierRepository.findById(id)
                .orElseThrow(() -> new AventurierNotFoundException("Aventurier non trouvé avec l'ID : " + id));

        // Appliquer les règles de progression du niveau
        validateNiveauProgression(aventurier.getNiveau(), dto.getNiveau());

        aventurier.setNom(dto.getNom());
        aventurier.setDescription(dto.getDescription());
        aventurier.setCaracteristiques(mapperService.toCaracteristiques(dto.getCaracteristiques()));
        aventurier.setNiveau(dto.getNiveau());
        aventurier.setClasse(dto.getClasse());

        aventurier = aventurierRepository.save(aventurier);
        return mapperService.toDTO(aventurier);
    }

    /**
     * Met à jour partiellement un aventurier (PATCH)
     * Règles métier appliquées :
     * - Le niveau ne peut jamais descendre
     * - Le niveau ne peut pas monter de plus de 1 à la fois
     */
    public AventurierDTO patchAventurier(UUID id, AventurierPatchDTO dto) {
        Aventurier aventurier = aventurierRepository.findById(id)
                .orElseThrow(() -> new AventurierNotFoundException("Aventurier non trouvé avec l'ID : " + id));

        if (dto.getNom() != null) {
            aventurier.setNom(dto.getNom());
        }

        if (dto.getDescription() != null) {
            aventurier.setDescription(dto.getDescription());
        }

        if (dto.getCaracteristiques() != null) {
            Caracteristiques currentCaracteristiques = aventurier.getCaracteristiques();
            if (dto.getCaracteristiques().getPhysique() != null) {
                currentCaracteristiques.setPhysique(dto.getCaracteristiques().getPhysique());
            }
            if (dto.getCaracteristiques().getMental() != null) {
                currentCaracteristiques.setMental(dto.getCaracteristiques().getMental());
            }
            if (dto.getCaracteristiques().getPerception() != null) {
                currentCaracteristiques.setPerception(dto.getCaracteristiques().getPerception());
            }
            aventurier.setCaracteristiques(currentCaracteristiques);
        }

        if (dto.getNiveau() != null) {
            validateNiveauProgression(aventurier.getNiveau(), dto.getNiveau());
            aventurier.setNiveau(dto.getNiveau());
        }

        if (dto.getClasse() != null) {
            aventurier.setClasse(dto.getClasse());
        }

        aventurier = aventurierRepository.save(aventurier);
        return mapperService.toDTO(aventurier);
    }

    /**
     * Supprime un aventurier
     */
    public void deleteAventurier(UUID id) {
        if (!aventurierRepository.existsById(id)) {
            throw new AventurierNotFoundException("Aventurier non trouvé avec l'ID : " + id);
        }
        aventurierRepository.deleteById(id);
    }

    /**
     * Valide les règles de progression du niveau
     * - Le niveau ne peut jamais descendre
     * - Le niveau ne peut pas monter de plus de 1 à la fois
     */
    private void validateNiveauProgression(Integer currentNiveau, Integer newNiveau) {
        if (newNiveau < currentNiveau) {
            throw new AventurierBusinessException("Le niveau ne peut jamais descendre. Niveau actuel : " + currentNiveau + ", Niveau demandé : " + newNiveau);
        }

        if (newNiveau > currentNiveau + 1) {
            throw new AventurierBusinessException("Le niveau ne peut pas monter de plus de 1 à la fois. Niveau actuel : " + currentNiveau + ", Niveau demandé : " + newNiveau);
        }
    }
}
