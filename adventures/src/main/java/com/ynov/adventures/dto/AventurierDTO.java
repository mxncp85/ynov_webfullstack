package com.ynov.adventures.dto;

import com.ynov.adventures.domain.Classe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AventurierDTO {

    private UUID id;

    private String nom;

    private String description;

    private CaracteristiquesDTO caracteristiques;

    private Integer niveau;

    private Classe classe;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
