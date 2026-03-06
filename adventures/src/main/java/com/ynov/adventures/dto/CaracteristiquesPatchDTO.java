package com.ynov.adventures.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaracteristiquesPatchDTO {

    @Min(value = 1, message = "La valeur doit être comprise entre 1 et 20")
    @Max(value = 20, message = "La valeur doit être comprise entre 1 et 20")
    private Integer physique;

    @Min(value = 1, message = "La valeur doit être comprise entre 1 et 20")
    @Max(value = 20, message = "La valeur doit être comprise entre 1 et 20")
    private Integer mental;

    @Min(value = 1, message = "La valeur doit être comprise entre 1 et 20")
    @Max(value = 20, message = "La valeur doit être comprise entre 1 et 20")
    private Integer perception;
}
