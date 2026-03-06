package com.ynov.adventures.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Caracteristiques {

    @Min(1)
    @Max(20)
    private Integer physique;

    @Min(1)
    @Max(20)
    private Integer mental;

    @Min(1)
    @Max(20)
    private Integer perception;
}
