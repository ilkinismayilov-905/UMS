package com.example.controller;

import com.example.dto.request.CreateGradeRequest;
import com.example.dto.request.UpdateGradeRequest;
import com.example.dto.response.*;
import com.example.enums.Department;
import com.example.service.GradeService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GradeControllerTest {

    @Mock
    private GradeService gradeService;

    @InjectMocks
    private GradeController gradeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    private GradeResponse gradeResponse;
    private CreateGradeRequest createGradeRequest;
    private UpdateGradeRequest updateGradeRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(gradeController).build();

        gradeResponse = new GradeResponse(
                1L,
                new StudentResponse(1L, "STU001", null, null),
                new SubjectResponse(1L, "Math", 3),
                new TeacherResponse(1L, null, Department.COMPUTER_SCIENCE),
                8,
                7,
                6,
                7,
                8,
                35,
                71,
                "EXCELLENT"
        );

        createGradeRequest = new CreateGradeRequest(
                1L,
                1L,
                1L,
                8,
                7,
                6,
                7,
                8,
                35
        );

        updateGradeRequest = new UpdateGradeRequest(
                9,
                8,
                7,
                8,
                9,
                37
        );
    }

    @Test
    void shouldGetAllGradesSuccessfully() throws Exception {
        // Arrange
        List<GradeResponse> grades = List.of(gradeResponse);
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
    void shouldUpdateGradeSuccessfully() throws Exception {
        // Arrange
        GradeResponse updatedResponse = new GradeResponse(
                1L,
                new StudentResponse(1L, "STU001", null, null),
                new SubjectResponse(1L, "Math", 3),
                new TeacherResponse(1L, null, Department.COMPUTER_SCIENCE),
                9,
                8,
                7,
                8,
                9,
                37,
                78,
                "EXCELLENT"
        );

        when(gradeService.updateGrade(eq(1L), any(UpdateGradeRequest.class))).thenReturn(updatedResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/grades/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateGradeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalScore").value(78));
    }

    @Test
    void shouldDeleteGradeSuccessfully() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/grades/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetGradesByStudentIdSuccessfully() throws Exception {
        // Arrange
        List<GradeResponse> grades = List.of(gradeResponse);
        when(gradeService.getGradesByStudentId(1L)).thenReturn(grades);

        // Act & Assert
        mockMvc.perform(get("/api/v1/grades/student/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalScore").value(71));
    }

    @Test
    void shouldGetGradesByTeacherIdSuccessfully() throws Exception {
        // Arrange
        List<GradeResponse> grades = List.of(gradeResponse);
        when(gradeService.getGradesByTeacherId(1L)).thenReturn(grades);

        // Act & Assert
        mockMvc.perform(get("/api/v1/grades/teacher/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalScore").value(71));
    }

    @Test
    void shouldReturnBadRequestWhenAttendanceScoreOutOfRange() throws Exception {
        // Arrange
        CreateGradeRequest invalidRequest = new CreateGradeRequest(
                1L,
                1L,
                1L,
                11, // Invalid: should be 0-10
                7,
                6,
                7,
                8,
                35
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenExamScoreOutOfRange() throws Exception {
        // Arrange
        CreateGradeRequest invalidRequest = new CreateGradeRequest(
                1L,
                1L,
                1L,
                8,
                7,
                6,
                7,
                8,
                51 // Invalid: should be 0-50
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

}


