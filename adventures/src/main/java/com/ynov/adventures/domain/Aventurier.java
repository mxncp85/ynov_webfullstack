package com.ynov.adventures.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "aventuriers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aventurier {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(length = 1000)
    private String description;

    @Embedded
    private Caracteristiques caracteristiques;

    @Column(nullable = false)
    private Integer niveau;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Classe classe;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (niveau == null) {
            niveau = 1;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
