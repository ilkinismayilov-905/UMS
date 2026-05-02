package com.example.controller;

import com.example.dto.request.CreateSubjectRequest;
import com.example.dto.request.UpdateSubjectRequest;
import com.example.dto.response.SubjectResponse;
import com.example.service.SubjectService;
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
class SubjectControllerTest {

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectController subjectController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private SubjectResponse subjectResponse;
    private CreateSubjectRequest createSubjectRequest;
    private UpdateSubjectRequest updateSubjectRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController).build();

        subjectResponse = new SubjectResponse(1L, "Mathematics", 3);
        createSubjectRequest = new CreateSubjectRequest("Mathematics", 3);
        updateSubjectRequest = new UpdateSubjectRequest(4);
    }

    @Test
    void shouldGetAllSubjectsSuccessfully() throws Exception {
        // Arrange
        List<SubjectResponse> subjects = List.of(subjectResponse);
        when(subjectService.getAllSubjects()).thenReturn(subjects);

        // Act & Assert
        mockMvc.perform(get("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Mathematics"))
                .andExpect(jsonPath("$[0].credits").value(3));
    }

    @Test
    void shouldGetSubjectByIdSuccessfully() throws Exception {
        // Arrange
        when(subjectService.getSubjectById(1L)).thenReturn(subjectResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/subjects/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Mathematics"));
    }

    @Test
    void shouldGetSubjectByNameSuccessfully() throws Exception {
        // Arrange
        when(subjectService.getSubjectByName("Mathematics")).thenReturn(subjectResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/subjects/name/Mathematics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mathematics"));
    }

    @Test
    void shouldCreateSubjectSuccessfully() throws Exception {
        // Arrange
        when(subjectService.createSubject(any(CreateSubjectRequest.class))).thenReturn(subjectResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSubjectRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Mathematics"))
                .andExpect(jsonPath("$.credits").value(3));
    }

    @Test
    void shouldUpdateSubjectSuccessfully() throws Exception {
        // Arrange
        SubjectResponse updatedResponse = new SubjectResponse(1L, "Mathematics", 4);
        when(subjectService.updateSubject(eq(1L), any(UpdateSubjectRequest.class))).thenReturn(updatedResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/subjects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateSubjectRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.credits").value(4));
    }

    @Test
    void shouldDeleteSubjectSuccessfully() throws Exception {
        // Arrange
        doNothing().when(subjectService).deleteSubject(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/subjects/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingSubjectWithBlankName() throws Exception {
        // Arrange
        CreateSubjectRequest invalidRequest = new CreateSubjectRequest("", 3);

        // Act & Assert
        mockMvc.perform(post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingSubjectWithNullCredits() throws Exception {
        // Arrange
        CreateSubjectRequest invalidRequest = new CreateSubjectRequest("Mathematics", null);

        // Act & Assert
        mockMvc.perform(post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingSubjectWithNullCredits() throws Exception {
        // Arrange
        UpdateSubjectRequest invalidRequest = new UpdateSubjectRequest(null);

        // Act & Assert
        mockMvc.perform(put("/api/v1/subjects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
