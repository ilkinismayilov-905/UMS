package com.example.controller;

import com.example.dto.request.CreateTeacherRequest;
import com.example.dto.request.UpdateTeacherRequest;
import com.example.dto.response.TeacherResponse;
import com.example.dto.response.UserResponse;
import com.example.enums.Department;
import com.example.service.TeacherService;
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
class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private TeacherResponse teacherResponse;
    private CreateTeacherRequest createTeacherRequest;
    private UpdateTeacherRequest updateTeacherRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        UserResponse userResponse = new UserResponse(
                1L,
                "teacher@school.com",
                "Jane",
                "Smith",
                "TEACHER",
                true
        );

        teacherResponse = new TeacherResponse(1L, userResponse, Department.COMPUTER_SCIENCE);
        createTeacherRequest = new CreateTeacherRequest(1L, Department.COMPUTER_SCIENCE);
        updateTeacherRequest = new UpdateTeacherRequest(Department.MATHEMATICS);
    }

    @Test
    void shouldGetAllTeachersSuccessfully() throws Exception {
        // Arrange
        List<TeacherResponse> teachers = List.of(teacherResponse);
        when(teacherService.getAllTeachers()).thenReturn(teachers);

        // Act & Assert
        mockMvc.perform(get("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].department").value("COMPUTER_SCIENCE"));
    }

    @Test
    void shouldGetTeacherByIdSuccessfully() throws Exception {
        // Arrange
        when(teacherService.getTeacherById(1L)).thenReturn(teacherResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.department").value("COMPUTER_SCIENCE"));
    }

    @Test
    void shouldGetTeacherByUserIdSuccessfully() throws Exception {
        // Arrange
        when(teacherService.getTeacherByUserId(1L)).thenReturn(teacherResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/teachers/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email").value("teacher@school.com"));
    }

    @Test
    void shouldCreateTeacherSuccessfully() throws Exception {
        // Arrange
        when(teacherService.createTeacher(any(CreateTeacherRequest.class))).thenReturn(teacherResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTeacherRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.department").value("COMPUTER_SCIENCE"));
    }

    @Test
    void shouldUpdateTeacherSuccessfully() throws Exception {
        // Arrange
        TeacherResponse updatedResponse = new TeacherResponse(1L,
                new UserResponse(1L, "teacher@school.com", "Jane", "Smith", "TEACHER", true),
                Department.MATHEMATICS);
        when(teacherService.updateTeacher(eq(1L), any(UpdateTeacherRequest.class))).thenReturn(updatedResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTeacherRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.department").value("MATHEMATICS"));
    }

    @Test
    void shouldDeleteTeacherSuccessfully() throws Exception {
        // Arrange
        doNothing().when(teacherService).deleteTeacher(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingTeacherWithNullUserId() throws Exception {
        // Arrange
        CreateTeacherRequest invalidRequest = new CreateTeacherRequest(null, Department.COMPUTER_SCIENCE);

        // Act & Assert
        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingTeacherWithNullDepartment() throws Exception {
        // Arrange
        CreateTeacherRequest invalidRequest = new CreateTeacherRequest(1L, null);

        // Act & Assert
        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
