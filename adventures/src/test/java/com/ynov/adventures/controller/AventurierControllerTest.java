package com.ynov.adventures.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynov.adventures.domain.Classe;
import com.ynov.adventures.dto.AventurierCreateDTO;
import com.ynov.adventures.dto.AventurierDTO;
import com.ynov.adventures.dto.AventurierListResponseDTO;
import com.ynov.adventures.dto.CaracteristiquesDTO;
import com.ynov.adventures.exception.AventurierNotFoundException;
import com.ynov.adventures.exception.GlobalExceptionHandler;
import com.ynov.adventures.service.AventurierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AventurierControllerTest {
    private MockMvc mockMvc;

    private AventurierService aventurierService;

    private ObjectMapper objectMapper;

    private UUID testId;
    private AventurierDTO testAventurierDTO;
    private AventurierCreateDTO testCreateDTO;

    @BeforeEach
    void setUp() {
        aventurierService = mock(AventurierService.class);
        AventurierController aventurierController = new AventurierController(aventurierService);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(aventurierController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
        objectMapper = new ObjectMapper();

        testId = UUID.randomUUID();

        testAventurierDTO = AventurierDTO.builder()
                .id(testId)
                .nom("Aragorn")
                .description("Rôdeur des terres sauvages")
                .caracteristiques(new CaracteristiquesDTO(15, 14, 16))
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
    void testCreateAventurier_Success() throws Exception {
        // Given
        when(aventurierService.createAventurier(any())).thenReturn(testAventurierDTO);

        // When & Then
        mockMvc.perform(post("/api/v1/aventuriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(testId.toString())))
                .andExpect(jsonPath("$.nom", is("Aragorn")))
                .andExpect(jsonPath("$.niveau", is(1)));

        verify(aventurierService, times(1)).createAventurier(any());
    }

    @Test
    void testCreateAventurier_InvalidData() throws Exception {
        // Given - Missing required fields
        String invalidPayload = "{ \"nom\": \"A\" }"; // Name too short

        // When & Then
        mockMvc.perform(post("/api/v1/aventuriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest());

        verify(aventurierService, times(0)).createAventurier(any());
    }

    @Test
    void testGetAventurierById_Success() throws Exception {
        // Given
        when(aventurierService.getAventurierById(testId)).thenReturn(testAventurierDTO);

        // When & Then
        mockMvc.perform(get("/api/v1/aventuriers/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testId.toString())))
                .andExpect(jsonPath("$.nom", is("Aragorn")));

        verify(aventurierService, times(1)).getAventurierById(testId);
    }

    @Test
    void testGetAventurierById_NotFound() throws Exception {
        // Given
        when(aventurierService.getAventurierById(testId))
                .thenThrow(new AventurierNotFoundException("Aventurier non trouvé"));

        // When & Then
        mockMvc.perform(get("/api/v1/aventuriers/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(aventurierService, times(1)).getAventurierById(testId);
    }

    @Test
    void testListAventuriers_Success() throws Exception {
        // Given
        AventurierListResponseDTO listResponse = AventurierListResponseDTO.builder()
                .data(Arrays.asList(testAventurierDTO))
                .pagination(null) // Would be set in real scenario
                .build();

        when(aventurierService.listAventuriers(1, 20, null, null, null))
                .thenReturn(listResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/aventuriers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].nom", is("Aragorn")));

        verify(aventurierService, times(1)).listAventuriers(1, 20, null, null, null);
    }

    @Test
    void testDeleteAventurier_Success() throws Exception {
        // Given
        doNothing().when(aventurierService).deleteAventurier(testId);

        // When & Then
        mockMvc.perform(delete("/api/v1/aventuriers/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(aventurierService, times(1)).deleteAventurier(testId);
    }

    @Test
    void testDeleteAventurier_NotFound() throws Exception {
        // Given
        doThrow(new AventurierNotFoundException("Aventurier non trouvé"))
                .when(aventurierService).deleteAventurier(testId);

        // When & Then
        mockMvc.perform(delete("/api/v1/aventuriers/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(aventurierService, times(1)).deleteAventurier(testId);
    }

}
