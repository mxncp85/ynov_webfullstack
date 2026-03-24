package com.ynov.adventures.controller;

import com.ynov.adventures.generated.api.AventuriersApi;
import com.ynov.adventures.generated.model.*;
import com.ynov.adventures.service.AventurierService;
import com.ynov.adventures.service.MapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AventurierController implements AventuriersApi {

    private final AventurierService aventurierService;
    private final MapperService mapperService;

    /**
     * GET /api/v1/aventuriers — VIEWER / ADMIN
     */
    @Override
    @PreAuthorize("hasAnyRole('VIEWER', 'ADMIN')")
    public ResponseEntity<AventurierListResponse> listAventuriers(
            Integer page,
            Integer limit,
            Classe classe,
            Integer niveauMin,
            Integer niveauMax
    ) {
        com.ynov.adventures.domain.Classe domainClasse = classe != null
                ? com.ynov.adventures.domain.Classe.valueOf(classe.name())
                : null;

        return ResponseEntity.ok(
                mapperService.toGeneratedListResponse(
                        aventurierService.listAventuriers(page, limit, domainClasse, niveauMin, niveauMax)
                )
        );
    }

    /**
     * POST /api/v1/aventuriers — ADMIN
     */
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Aventurier> createAventurier(AventurierCreateInput aventurierCreateInput) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapperService.toGeneratedAventurier(
                        aventurierService.createAventurier(mapperService.fromGenerated(aventurierCreateInput))
                )
        );
    }

    /**
     * GET /api/v1/aventuriers/{id} — VIEWER / ADMIN
     */
    @Override
    @PreAuthorize("hasAnyRole('VIEWER', 'ADMIN')")
    public ResponseEntity<Aventurier> getAventurierById(UUID id) {
        return ResponseEntity.ok(
                mapperService.toGeneratedAventurier(aventurierService.getAventurierById(id))
        );
    }

    /**
     * PUT /api/v1/aventuriers/{id} — ADMIN
     */
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Aventurier> updateAventurier(UUID id, AventurierCreate aventurierCreate) {
        return ResponseEntity.ok(
                mapperService.toGeneratedAventurier(
                        aventurierService.updateAventurier(id, mapperService.fromGenerated(aventurierCreate))
                )
        );
    }

    /**
     * PATCH /api/v1/aventuriers/{id} — ADMIN
     */
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Aventurier> patchAventurier(UUID id, AventurierPatch aventurierPatch) {
        return ResponseEntity.ok(
                mapperService.toGeneratedAventurier(
                        aventurierService.patchAventurier(id, mapperService.fromGenerated(aventurierPatch))
                )
        );
    }

    /**
     * DELETE /api/v1/aventuriers/{id} — ADMIN
     */
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAventurier(UUID id) {
        aventurierService.deleteAventurier(id);
        return ResponseEntity.noContent().build();
    }
}
