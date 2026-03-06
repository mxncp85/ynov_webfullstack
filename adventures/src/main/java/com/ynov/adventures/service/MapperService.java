package com.ynov.adventures.service;

import com.ynov.adventures.domain.Aventurier;
import com.ynov.adventures.domain.Caracteristiques;
import com.ynov.adventures.dto.AventurierDTO;
import com.ynov.adventures.dto.CaracteristiquesDTO;
import org.springframework.stereotype.Service;

@Service
public class MapperService {

    public AventurierDTO toDTO(Aventurier aventurier) {
        if (aventurier == null) {
            return null;
        }

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
        if (dto == null) {
            return null;
        }

        return new Caracteristiques(
                dto.getPhysique(),
                dto.getMental(),
                dto.getPerception()
        );
    }

    public CaracteristiquesDTO toCaracteristiquesDTO(Caracteristiques caracteristiques) {
        if (caracteristiques == null) {
            return null;
        }

        return CaracteristiquesDTO.builder()
                .physique(caracteristiques.getPhysique())
                .mental(caracteristiques.getMental())
                .perception(caracteristiques.getPerception())
                .build();
    }
}
