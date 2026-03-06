package com.ynov.adventures.dto;

import com.ynov.adventures.domain.Classe;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AventurierPatchDTO {

    @Size(min = 2, max = 100, message = "Le nom doit faire entre 2 et 100 caractères")
    private String nom;

    @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères")
    private String description;

    @Valid
    private CaracteristiquesPatchDTO caracteristiques;

    @Min(value = 1, message = "Le niveau minimum est 1")
    @Max(value = 20, message = "Le niveau maximum est 20")
    private Integer niveau;

    private Classe classe;
}
