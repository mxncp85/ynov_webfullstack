package com.ynov.adventures.service;

import com.ynov.adventures.domain.Aventurier;
import com.ynov.adventures.domain.Caracteristiques;
import com.ynov.adventures.domain.Classe;
import com.ynov.adventures.dto.*;
import com.ynov.adventures.generated.model.*;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Service
public class MapperService {

    // ─────────────────────────────────────────
    // Domain → Internal DTO
    // ─────────────────────────────────────────

    public AventurierDTO toDTO(Aventurier aventurier) {
        if (aventurier == null) return null;

        return AventurierDTO.builder()
                .id(aventurier.getId())
                .nom(aventurier.getNom())
                .description(aventurier.getDescription())
                .caracteristiques(toCaracteristiquesDTO(aventurier.getCaracteristiques()))
                .niveau(aventurier.getNiveau())
                .classe(aventurier.getClasse())
                .createdAt(aventurier.getCreatedAt())
                .updatedAt(aventurier.getUpdatedAt())
                .build();
    }

    public Caracteristiques toCaracteristiques(CaracteristiquesDTO dto) {
        if (dto == null) return null;
        return new Caracteristiques(dto.getPhysique(), dto.getMental(), dto.getPerception());
    }

    public CaracteristiquesDTO toCaracteristiquesDTO(Caracteristiques caracteristiques) {
        if (caracteristiques == null) return null;
        return CaracteristiquesDTO.builder()
                .physique(caracteristiques.getPhysique())
                .mental(caracteristiques.getMental())
                .perception(caracteristiques.getPerception())
                .build();
    }

    // ─────────────────────────────────────────
    // Generated model → Internal DTO
    // ─────────────────────────────────────────

    public AventurierCreateDTO fromGenerated(AventurierCreateInput input) {
        if (input == null) return null;
        return AventurierCreateDTO.builder()
                .nom(input.getNom())
                .description(input.getDescription())
                .caracteristiques(fromGeneratedCaracteristiques(input.getCaracteristiques()))
                .classe(toDomaineClasse(input.getClasse()))
                .build();
    }

    public AventurierUpdateDTO fromGenerated(AventurierCreate create) {
        if (create == null) return null;
        return AventurierUpdateDTO.builder()
                .nom(create.getNom())
                .description(create.getDescription())
                .caracteristiques(fromGeneratedCaracteristiques(create.getCaracteristiques()))
                .niveau(create.getNiveau())
                .classe(toDomaineClasse(create.getClasse()))
                .build();
    }

    public AventurierPatchDTO fromGenerated(AventurierPatch patch) {
        if (patch == null) return null;

        AventurierPatchDTO.AventurierPatchDTOBuilder builder = AventurierPatchDTO.builder()
                .nom(patch.getNom())
                .niveau(patch.getNiveau())
                .classe(patch.getClasse() != null ? toDomaineClasse(patch.getClasse()) : null);

        // JsonNullable : présent = la valeur a été explicitement envoyée (peut être null)
        JsonNullable<String> description = patch.getDescription();
        if (description != null && description.isPresent()) {
            builder.description(description.get());
        }

        if (patch.getCaracteristiques() != null) {
            builder.caracteristiques(fromGeneratedCaracteristiquesPatch(patch.getCaracteristiques()));
        }

        return builder.build();
    }

    // ─────────────────────────────────────────
    // Internal DTO → Generated model
    // ─────────────────────────────────────────

    public com.ynov.adventures.generated.model.Aventurier toGeneratedAventurier(AventurierDTO dto) {
        if (dto == null) return null;

        com.ynov.adventures.generated.model.Caracteristiques genCarac = null;
        if (dto.getCaracteristiques() != null) {
            genCarac = new com.ynov.adventures.generated.model.Caracteristiques()
                    .physique(dto.getCaracteristiques().getPhysique())
                    .mental(dto.getCaracteristiques().getMental())
                    .perception(dto.getCaracteristiques().getPerception());
        }

        return new com.ynov.adventures.generated.model.Aventurier()
                .id(dto.getId())
                .nom(dto.getNom())
                .description(dto.getDescription())
                .caracteristiques(genCarac)
                .niveau(dto.getNiveau())
                .classe(toGeneratedClasse(dto.getClasse()))
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt().atOffset(ZoneOffset.UTC) : null)
                .updatedAt(dto.getUpdatedAt() != null ? dto.getUpdatedAt().atOffset(ZoneOffset.UTC) : null);
    }

    public AventurierListResponse toGeneratedListResponse(AventurierListResponseDTO dto) {
        if (dto == null) return null;

        Pagination pagination = new Pagination()
                .page(dto.getPagination().getPage())
                .limit(dto.getPagination().getLimit())
                .total(dto.getPagination().getTotal())
                .totalPages(dto.getPagination().getTotalPages());

        return new AventurierListResponse()
                .data(dto.getData().stream()
                        .map(this::toGeneratedAventurier)
                        .collect(Collectors.toList()))
                .pagination(pagination);
    }

    // ─────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────

    private CaracteristiquesDTO fromGeneratedCaracteristiques(
            com.ynov.adventures.generated.model.Caracteristiques gen) {
        if (gen == null) return null;
        return CaracteristiquesDTO.builder()
                .physique(gen.getPhysique())
                .mental(gen.getMental())
                .perception(gen.getPerception())
                .build();
    }

    private CaracteristiquesPatchDTO fromGeneratedCaracteristiquesPatch(
            com.ynov.adventures.generated.model.CaracteristiquesPatch gen) {
        if (gen == null) return null;
        return CaracteristiquesPatchDTO.builder()
                .physique(gen.getPhysique())
                .mental(gen.getMental())
                .perception(gen.getPerception())
                .build();
    }

    private Classe toDomaineClasse(com.ynov.adventures.generated.model.Classe generated) {
        if (generated == null) return null;
        return Classe.valueOf(generated.name());
    }

    private com.ynov.adventures.generated.model.Classe toGeneratedClasse(Classe domaine) {
        if (domaine == null) return null;
        return com.ynov.adventures.generated.model.Classe.valueOf(domaine.name());
    }
}
