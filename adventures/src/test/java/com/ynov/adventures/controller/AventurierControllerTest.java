package com.ynov.adventures.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynov.adventures.domain.Classe;
import com.ynov.adventures.dto.*;
import com.ynov.adventures.exception.AventurierNotFoundException;
import com.ynov.adventures.exception.GlobalExceptionHandler;
import com.ynov.adventures.generated.model.*;
import com.ynov.adventures.service.AventurierService;
import com.ynov.adventures.service.MapperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AventurierControllerTest {

    private MockMvc mockMvc;
    private AventurierService aventurierService;
    private MapperService mapperService;
    private ObjectMapper objectMapper;

    private UUID testId;
    private AventurierDTO testAventurierDTO;

    /** Modèle généré retourné par le mapper (ce que le controller sérialise). */
    private Aventurier testGeneratedAventurier;

    @BeforeEach
    void setUp() {
        aventurierService = mock(AventurierService.class);
        mapperService     = mock(MapperService.class);

        AventurierController controller = new AventurierController(aventurierService, mapperService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // support OffsetDateTime

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

        testGeneratedAventurier = new Aventurier()
                .id(testId)
                .nom("Aragorn")
                .description("Rôdeur des terres sauvages")
                .caracteristiques(new Caracteristiques().physique(15).mental(14).perception(16))
                .niveau(1)
                .classe(com.ynov.adventures.generated.model.Classe.RODEUR)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now());
    }

    @Test
    void testCreateAventurier_Success() throws Exception {
        when(aventurierService.createAventurier(any())).thenReturn(testAventurierDTO);
        when(mapperService.fromGenerated(any(AventurierCreateInput.class))).thenReturn(
                AventurierCreateDTO.builder()
                        .nom("Aragorn")
                        .caracteristiques(new CaracteristiquesDTO(15, 14, 16))
                        .classe(Classe.RODEUR)
                        .build()
        );
        when(mapperService.toGeneratedAventurier(testAventurierDTO)).thenReturn(testGeneratedAventurier);

        String payload = """
                {
                  "nom": "Aragorn",
                  "caracteristiques": { "physique": 15, "mental": 14, "perception": 16 },
                  "classe": "Rodeur"
                }
                """;

        mockMvc.perform(post("/api/v1/aventuriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(testId.toString())))
                .andExpect(jsonPath("$.nom", is("Aragorn")))
                .andExpect(jsonPath("$.niveau", is(1)));

        verify(aventurierService, times(1)).createAventurier(any());
    }

    @Test
    void testGetAventurierById_Success() throws Exception {
        when(aventurierService.getAventurierById(testId)).thenReturn(testAventurierDTO);
        when(mapperService.toGeneratedAventurier(testAventurierDTO)).thenReturn(testGeneratedAventurier);

        mockMvc.perform(get("/api/v1/aventuriers/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testId.toString())))
                .andExpect(jsonPath("$.nom", is("Aragorn")));

        verify(aventurierService, times(1)).getAventurierById(testId);
    }

    @Test
    void testGetAventurierById_NotFound() throws Exception {
        when(aventurierService.getAventurierById(testId))
                .thenThrow(new AventurierNotFoundException("Aventurier non trouvé"));

        mockMvc.perform(get("/api/v1/aventuriers/{id}", testId))
                .andExpect(status().isNotFound());

        verify(aventurierService, times(1)).getAventurierById(testId);
    }

    @Test
    void testListAventuriers_Success() throws Exception {
        AventurierListResponseDTO listResponseDTO = AventurierListResponseDTO.builder()
                .data(List.of(testAventurierDTO))
                .pagination(PaginationDTO.builder().page(1).limit(20).total(1).totalPages(1).build())
                .build();

        AventurierListResponse generatedResponse = new AventurierListResponse()
                .data(List.of(testGeneratedAventurier))
                .pagination(new Pagination().page(1).limit(20).total(1).totalPages(1));

        when(aventurierService.listAventuriers(1, 20, null, null, null)).thenReturn(listResponseDTO);
        when(mapperService.toGeneratedListResponse(listResponseDTO)).thenReturn(generatedResponse);

        mockMvc.perform(get("/api/v1/aventuriers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].nom", is("Aragorn")));

        verify(aventurierService, times(1)).listAventuriers(1, 20, null, null, null);
    }

    @Test
    void testDeleteAventurier_Success() throws Exception {
        doNothing().when(aventurierService).deleteAventurier(testId);

        mockMvc.perform(delete("/api/v1/aventuriers/{id}", testId))
                .andExpect(status().isNoContent());

        verify(aventurierService, times(1)).deleteAventurier(testId);
    }

    @Test
    void testDeleteAventurier_NotFound() throws Exception {
        doThrow(new AventurierNotFoundException("Aventurier non trouvé"))
                .when(aventurierService).deleteAventurier(testId);

        mockMvc.perform(delete("/api/v1/aventuriers/{id}", testId))
                .andExpect(status().isNotFound());

        verify(aventurierService, times(1)).deleteAventurier(testId);
    }
}
