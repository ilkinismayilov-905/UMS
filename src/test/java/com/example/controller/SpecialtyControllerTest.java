package com.example.controller;

import com.example.dto.request.CreateSpecialtyRequest;
import com.example.dto.request.UpdateSpecialtyRequest;
import com.example.dto.response.SpecialtyResponse;
import com.example.service.SpecialtyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SpecialtyControllerTest {

    @Mock
    private SpecialtyService specialtyService;

    @InjectMocks
    private SpecialtyController specialtyController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private SpecialtyResponse specialtyResponse;
    private CreateSpecialtyRequest createSpecialtyRequest;
    private UpdateSpecialtyRequest updateSpecialtyRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(specialtyController).build();

        specialtyResponse = new SpecialtyResponse(1L, "Computer Science");
        createSpecialtyRequest = new CreateSpecialtyRequest("Computer Science");
        updateSpecialtyRequest = new UpdateSpecialtyRequest("Data Science");
    }

    @Test
    void shouldGetAllSpecialtiesSuccessfully() throws Exception {
        // Arrange
        List<SpecialtyResponse> specialties = List.of(specialtyResponse);
        when(specialtyService.getAllSpecialties()).thenReturn(specialties);

        // Act & Assert
        mockMvc.perform(get("/api/v1/specialties")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Computer Science"));
    }

    @Test
    void shouldGetSpecialtyByIdSuccessfully() throws Exception {
        // Arrange
        when(specialtyService.getSpecialtyById(1L)).thenReturn(specialtyResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/specialties/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Computer Science"));
    }

    @Test
    void shouldGetSpecialtyByNameSuccessfully() throws Exception {
        // Arrange
        when(specialtyService.getSpecialtyByName("Computer Science")).thenReturn(specialtyResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/specialties/name/Computer Science")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Computer Science"));
    }

    @Test
    void shouldCreateSpecialtySuccessfully() throws Exception {
        // Arrange
        when(specialtyService.createSpecialty(any(CreateSpecialtyRequest.class))).thenReturn(specialtyResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSpecialtyRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Computer Science"));
    }

    @Test
    void shouldUpdateSpecialtySuccessfully() throws Exception {
        // Arrange
        SpecialtyResponse updatedResponse = new SpecialtyResponse(1L, "Data Science");
        when(specialtyService.updateSpecialty(eq(1L), any(UpdateSpecialtyRequest.class))).thenReturn(updatedResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/specialties/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateSpecialtyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Data Science"));
    }

    @Test
    void shouldDeleteSpecialtySuccessfully() throws Exception {
        // Arrange
        doNothing().when(specialtyService).deleteSpecialty(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/specialties/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingSpecialtyWithBlankName() throws Exception {
        // Arrange
        CreateSpecialtyRequest invalidRequest = new CreateSpecialtyRequest("");

        // Act & Assert
        mockMvc.perform(post("/api/v1/specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingSpecialtyWithBlankName() throws Exception {
        // Arrange
        UpdateSpecialtyRequest invalidRequest = new UpdateSpecialtyRequest("");

        // Act & Assert
        mockMvc.perform(put("/api/v1/specialties/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
