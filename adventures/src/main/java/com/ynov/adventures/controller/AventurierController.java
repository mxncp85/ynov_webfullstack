package com.ynov.adventures.controller;

import com.ynov.adventures.domain.Classe;
import com.ynov.adventures.dto.*;
import com.ynov.adventures.service.AventurierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/aventuriers")
@RequiredArgsConstructor
public class AventurierController {

    private final AventurierService aventurierService;

    /**
     * GET /api/v1/aventuriers - Liste tous les aventuriers avec pagination et filtres
     */
    @GetMapping
    public ResponseEntity<AventurierListResponseDTO> listAventuriers(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "limit", defaultValue = "20") Integer limit,
            @RequestParam(name = "classe", required = false) Classe classe,
            @RequestParam(name = "niveau_min", required = false) Integer niveauMin,
            @RequestParam(name = "niveau_max", required = false) Integer niveauMax
    ) {
        AventurierListResponseDTO response = aventurierService.listAventuriers(page, limit, classe, niveauMin, niveauMax);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/v1/aventuriers - Crée un nouvel aventurier
     */
    @PostMapping
    public ResponseEntity<AventurierDTO> createAventurier(
            @Valid @RequestBody AventurierCreateDTO dto
    ) {
        AventurierDTO createdAventurier = aventurierService.createAventurier(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAventurier);
    }

    /**
     * GET /api/v1/aventuriers/{id} - Récupère un aventurier par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AventurierDTO> getAventurierById(
            @PathVariable UUID id
    ) {
        AventurierDTO aventurier = aventurierService.getAventurierById(id);
        return ResponseEntity.ok(aventurier);
    }

    /**
     * PUT /api/v1/aventuriers/{id} - Met à jour complètement un aventurier
     */
    @PutMapping("/{id}")
    public ResponseEntity<AventurierDTO> updateAventurier(
            @PathVariable UUID id,
            @Valid @RequestBody AventurierUpdateDTO dto
    ) {
        AventurierDTO updatedAventurier = aventurierService.updateAventurier(id, dto);
        return ResponseEntity.ok(updatedAventurier);
    }

    /**
     * PATCH /api/v1/aventuriers/{id} - Met à jour partiellement un aventurier
     */
    @PatchMapping("/{id}")
    public ResponseEntity<AventurierDTO> patchAventurier(
            @PathVariable UUID id,
            @Valid @RequestBody AventurierPatchDTO dto
    ) {
        AventurierDTO patchedAventurier = aventurierService.patchAventurier(id, dto);
        return ResponseEntity.ok(patchedAventurier);
    }

    /**
     * DELETE /api/v1/aventuriers/{id} - Supprime un aventurier
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAventurier(
            @PathVariable UUID id
    ) {
        aventurierService.deleteAventurier(id);
        return ResponseEntity.noContent().build();
    }
}
