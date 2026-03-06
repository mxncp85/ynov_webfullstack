package com.ynov.adventures.dto;

import com.ynov.adventures.domain.Classe;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AventurierUpdateDTO {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit faire entre 2 et 100 caracteres")
    private String nom;

    @Size(max = 1000, message = "La description ne peut pas depasser 1000 caracteres")
    private String description;

    @NotNull(message = "Les caracteristiques sont obligatoires")
    @Valid
    private CaracteristiquesDTO caracteristiques;

    @NotNull(message = "Le niveau est obligatoire")
    @Min(value = 1, message = "Le niveau minimum est 1")
    @Max(value = 20, message = "Le niveau maximum est 20")
    private Integer niveau;

    @NotNull(message = "La classe est obligatoire")
    private Classe classe;
}
