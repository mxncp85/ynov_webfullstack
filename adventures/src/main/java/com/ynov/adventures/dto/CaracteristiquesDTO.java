package com.ynov.adventures.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaracteristiquesDTO {

    @NotNull(message = "La caractéristique physique est obligatoire")
    @Min(value = 1, message = "La valeur doit être comprise entre 1 et 20")
    @Max(value = 20, message = "La valeur doit être comprise entre 1 et 20")
    private Integer physique;

    @NotNull(message = "La caractéristique mentale est obligatoire")
    @Min(value = 1, message = "La valeur doit être comprise entre 1 et 20")
    @Max(value = 20, message = "La valeur doit être comprise entre 1 et 20")
    private Integer mental;

    @NotNull(message = "La caractéristique de perception est obligatoire")
    @Min(value = 1, message = "La valeur doit être comprise entre 1 et 20")
    @Max(value = 20, message = "La valeur doit être comprise entre 1 et 20")
    private Integer perception;
}
