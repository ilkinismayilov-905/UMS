package com.example.controller;

import com.example.dto.request.CreateGradeRequest;
import com.example.dto.request.UpdateGradeRequest;
import com.example.dto.response.GradeResponse;
import com.example.service.GradeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GradeService gradeService;

    private GradeResponse gradeResponse;
    private CreateGradeRequest createGradeRequest;
    private UpdateGradeRequest updateGradeRequest;

    @BeforeEach
    void setUp() {
        gradeResponse = GradeResponse.builder()
                .id(1L)
                .studentId(1L)
                .subjectId(1L)
                .teacherId(1L)
                .attendanceScore(8)
                .seminarScore(7)
                .col1(6)
                .col2(7)
                .col3(8)
                .examScore(35)
                .totalScore(71)
                .status("EXCELLENT")
                .build();

        createGradeRequest = CreateGradeRequest.builder()
                .studentId(1L)
                .subjectId(1L)
                .teacherId(1L)
                .attendanceScore(8)
                .seminarScore(7)
                .col1(6)
                .col2(7)
                .col3(8)
                .examScore(35)
                .build();

        updateGradeRequest = UpdateGradeRequest.builder()
                .attendanceScore(9)
                .seminarScore(8)
                .col1(7)
                .col2(8)
                .col3(9)
                .examScore(37)
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllGradesSuccessfully() throws Exception {
        // Arrange
        List<GradeResponse> grades = Arrays.asList(gradeResponse);
        when(gradeService.getAllGrades()).thenReturn(grades);

        // Act & Assert
        mockMvc.perform(get("/api/v1/grades")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].totalScore").value(71))
                .andExpect(jsonPath("$[0].status").value("EXCELLENT"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetGradeByIdSuccessfully() throws Exception {
        // Arrange
        when(gradeService.getGradeById(1L)).thenReturn(gradeResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/grades/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalScore").value(71))
                .andExpect(jsonPath("$.status").value("EXCELLENT"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void shouldCreateGradeSuccessfully() throws Exception {
        // Arrange
        when(gradeService.createGrade(any(CreateGradeRequest.class))).thenReturn(gradeResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createGradeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalScore").value(71))
                .andExpect(jsonPath("$.status").value("EXCELLENT"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void shouldUpdateGradeSuccessfully() throws Exception {
        // Arrange
        GradeResponse updatedResponse = GradeResponse.builder()
                .id(1L)
                .studentId(1L)
                .subjectId(1L)
                .teacherId(1L)
                .attendanceScore(9)
                .seminarScore(8)
                .col1(7)
                .col2(8)
                .col3(9)
                .examScore(37)
                .totalScore(78)
                .status("EXCELLENT")
                .build();

        when(gradeService.updateGrade(eq(1L), any(UpdateGradeRequest.class))).thenReturn(updatedResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/grades/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateGradeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalScore").value(78));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void shouldDeleteGradeSuccessfully() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/grades/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void shouldGetGradesByStudentIdSuccessfully() throws Exception {
        // Arrange
        List<GradeResponse> grades = Arrays.asList(gradeResponse);
        when(gradeService.getGradesByStudentId(1L)).thenReturn(grades);

        // Act & Assert
        mockMvc.perform(get("/api/v1/grades/student/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalScore").value(71));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void shouldGetGradesByTeacherIdSuccessfully() throws Exception {
        // Arrange
        List<GradeResponse> grades = Arrays.asList(gradeResponse);
        when(gradeService.getGradesByTeacherId(1L)).thenReturn(grades);

        // Act & Assert
        mockMvc.perform(get("/api/v1/grades/teacher/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalScore").value(71));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void shouldReturnBadRequestWhenAttendanceScoreOutOfRange() throws Exception {
        // Arrange
        CreateGradeRequest invalidRequest = CreateGradeRequest.builder()
                .studentId(1L)
                .subjectId(1L)
                .teacherId(1L)
                .attendanceScore(11) // Invalid: should be 0-10
                .seminarScore(7)
                .col1(6)
                .col2(7)
                .col3(8)
                .examScore(35)
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void shouldReturnBadRequestWhenExamScoreOutOfRange() throws Exception {
        // Arrange
        CreateGradeRequest invalidRequest = CreateGradeRequest.builder()
                .studentId(1L)
                .subjectId(1L)
                .teacherId(1L)
                .attendanceScore(8)
                .seminarScore(7)
                .col1(6)
                .col2(7)
                .col3(8)
                .examScore(51) // Invalid: should be 0-50
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void shouldReturnForbiddenWhenStudentTriesToCreateGrade() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createGradeRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnUnauthorizedWhenAccessingWithoutToken() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/grades")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}


