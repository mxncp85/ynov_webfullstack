package com.ynov.adventures.service;

import com.ynov.adventures.domain.Aventurier;
import com.ynov.adventures.domain.Caracteristiques;
import com.ynov.adventures.domain.Classe;
import com.ynov.adventures.dto.AventurierCreateDTO;
import com.ynov.adventures.dto.AventurierDTO;
import com.ynov.adventures.dto.AventurierUpdateDTO;
import com.ynov.adventures.dto.CaracteristiquesDTO;
import com.ynov.adventures.exception.AventurierBusinessException;
import com.ynov.adventures.exception.AventurierNotFoundException;
import com.ynov.adventures.repository.AventurierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AventurierServiceTest {

    @Mock
    private AventurierRepository aventurierRepository;

    @Mock
    private MapperService mapperService;

    @InjectMocks
    private AventurierService aventurierService;

    private UUID testId;
    private Aventurier testAventurier;
    private AventurierCreateDTO testCreateDTO;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();

        testAventurier = Aventurier.builder()
                .id(testId)
                .nom("Aragorn")
                .description("Rôdeur des terres sauvages")
                .caracteristiques(new Caracteristiques(15, 14, 16))
                .niveau(1)
                .classe(Classe.RODEUR)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testCreateDTO = AventurierCreateDTO.builder()
                .nom("Aragorn")
                .description("Rôdeur des terres sauvages")
                .caracteristiques(new CaracteristiquesDTO(15, 14, 16))
                .classe(Classe.RODEUR)
                .build();
    }

    @Test
    void testCreateAventurier_WithLevel1_Success() {
        // Given
        when(mapperService.toCaracteristiques(any())).thenReturn(new Caracteristiques(15, 14, 16));
        when(aventurierRepository.save(any(Aventurier.class))).thenReturn(testAventurier);
        when(mapperService.toDTO(testAventurier)).thenReturn(
                AventurierDTO.builder()
                        .id(testId)
                        .nom("Aragorn")
                        .niveau(1)
                        .classe(Classe.RODEUR)
                        .build()
        );

        // When
        AventurierDTO result = aventurierService.createAventurier(testCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getNiveau());
        verify(aventurierRepository, times(1)).save(any(Aventurier.class));
    }

    @Test
    void testGetAventurierById_NotFound_ThrowsException() {
        // Given
        when(aventurierRepository.findById(testId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(AventurierNotFoundException.class, () -> {
            aventurierService.getAventurierById(testId);
        });
    }

    @Test
    void testUpdateNiveau_LevelDecrease_ThrowsException() {
        // Given
        testAventurier.setNiveau(5);
        AventurierUpdateDTO updateDTO = AventurierUpdateDTO.builder()
                .nom("Aragorn")
                .niveau(3) // Trying to decrease
                .classe(Classe.RODEUR)
                .caracteristiques(new CaracteristiquesDTO(15, 14, 16))
                .build();

        when(aventurierRepository.findById(testId)).thenReturn(Optional.of(testAventurier));

        // When & Then
        assertThrows(AventurierBusinessException.class, () -> {
            aventurierService.updateAventurier(testId, updateDTO);
        });
    }

    @Test
    void testUpdateNiveau_LevelIncreaseMoreThanOne_ThrowsException() {
        // Given
        testAventurier.setNiveau(5);
        AventurierUpdateDTO updateDTO = AventurierUpdateDTO.builder()
                .nom("Aragorn")
                .niveau(7) // Trying to increase by 2
                .classe(Classe.RODEUR)
                .caracteristiques(new CaracteristiquesDTO(15, 14, 16))
                .build();

        when(aventurierRepository.findById(testId)).thenReturn(Optional.of(testAventurier));

        // When & Then
        assertThrows(AventurierBusinessException.class, () -> {
            aventurierService.updateAventurier(testId, updateDTO);
        });
    }

    @Test
    void testUpdateNiveau_LevelIncreaseByOne_Success() {
        // Given
        testAventurier.setNiveau(5);
        AventurierUpdateDTO updateDTO = AventurierUpdateDTO.builder()
                .nom("Aragorn")
                .niveau(6) // Increase by 1
                .classe(Classe.RODEUR)
                .caracteristiques(new CaracteristiquesDTO(15, 14, 16))
                .build();

        when(aventurierRepository.findById(testId)).thenReturn(Optional.of(testAventurier));
        when(mapperService.toCaracteristiques(any())).thenReturn(new Caracteristiques(15, 14, 16));
        when(aventurierRepository.save(any(Aventurier.class))).thenReturn(testAventurier);
        when(mapperService.toDTO(any())).thenReturn(AventurierDTO.builder().id(testId).niveau(6).build());

        // When
        AventurierDTO result = aventurierService.updateAventurier(testId, updateDTO);

        // Then
        assertNotNull(result);
        verify(aventurierRepository, times(1)).save(any(Aventurier.class));
    }
}
