package com.example.controller;

import com.example.dto.request.CreateTeacherGroupSubjectRequest;
import com.example.dto.response.*;
import com.example.enums.Department;
import com.example.service.TeacherGroupSubjectService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TeacherGroupSubjectControllerTest {

    @Mock
    private TeacherGroupSubjectService tgsService;

    @InjectMocks
    private TeacherGroupSubjectController teacherGroupSubjectController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private TeacherGroupSubjectResponse tgsResponse;
    private CreateTeacherGroupSubjectRequest createRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(teacherGroupSubjectController).build();

        TeacherResponse teacherResponse = new TeacherResponse(1L, null, Department.ENGINEERING);
        GroupResponse groupResponse = new GroupResponse(1L, "651.21", new SpecialtyResponse(1L, "CS"));
        SubjectResponse subjectResponse = new SubjectResponse(1L, "Math", 3);

        tgsResponse = new TeacherGroupSubjectResponse(1L, teacherResponse, groupResponse, subjectResponse);
        createRequest = new CreateTeacherGroupSubjectRequest(1L, 1L, 1L);
    }

    @Test
    void shouldGetAllTeacherGroupSubjectsSuccessfully() throws Exception {
        // Arrange
        List<TeacherGroupSubjectResponse> list = List.of(tgsResponse);
        when(tgsService.getAllTeacherGroupSubjects()).thenReturn(list);

        // Act & Assert
        mockMvc.perform(get("/api/v1/teacher-group-subjects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].subject.name").value("Math"));
    }

    @Test
    void shouldGetTeacherGroupSubjectByIdSuccessfully() throws Exception {
        // Arrange
        when(tgsService.getTeacherGroupSubjectById(1L)).thenReturn(tgsResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/teacher-group-subjects/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.group.groupNumber").value("651.21"));
    }

    @Test
    void shouldGetTeacherGroupSubjectsByTeacherIdSuccessfully() throws Exception {
        // Arrange
        List<TeacherGroupSubjectResponse> list = List.of(tgsResponse);
        when(tgsService.getTeacherGroupSubjectsByTeacherId(1L)).thenReturn(list);

        // Act & Assert
        mockMvc.perform(get("/api/v1/teacher-group-subjects/teacher/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teacher.department").value("ENGINEERING"));
    }

    @Test
    void shouldCreateTeacherGroupSubjectSuccessfully() throws Exception {
        // Arrange
        when(tgsService.createTeacherGroupSubject(any(CreateTeacherGroupSubjectRequest.class))).thenReturn(tgsResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/teacher-group-subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldDeleteTeacherGroupSubjectSuccessfully() throws Exception {
        // Arrange
        doNothing().when(tgsService).deleteTeacherGroupSubject(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/teacher-group-subjects/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingWithNullTeacherId() throws Exception {
        // Arrange
        CreateTeacherGroupSubjectRequest invalid = new CreateTeacherGroupSubjectRequest(null, 1L, 1L);

        // Act & Assert
        mockMvc.perform(post("/api/v1/teacher-group-subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingWithNullGroupId() throws Exception {
        // Arrange
        CreateTeacherGroupSubjectRequest invalid = new CreateTeacherGroupSubjectRequest(1L, null, 1L);

        // Act & Assert
        mockMvc.perform(post("/api/v1/teacher-group-subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingWithNullSubjectId() throws Exception {
        // Arrange
        CreateTeacherGroupSubjectRequest invalid = new CreateTeacherGroupSubjectRequest(1L, 1L, null);

        // Act & Assert
        mockMvc.perform(post("/api/v1/teacher-group-subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}
