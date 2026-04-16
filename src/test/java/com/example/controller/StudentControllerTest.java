package com.example.controller;

import com.example.dto.request.CreateStudentRequest;
import com.example.dto.response.StudentResponse;
import com.example.dto.response.UserResponse;
import com.example.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    private StudentResponse studentResponse;
    private CreateStudentRequest createStudentRequest;

    @BeforeEach
    void setUp() {
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .email("student@school.com")
                .firstName("John")
                .lastName("Doe")
                .role("STUDENT")
                .isActive(true)
                .build();

        studentResponse = StudentResponse.builder()
                .id(1L)
                .user(userResponse)
                .studentNumber("STU001")
                .build();

        createStudentRequest = CreateStudentRequest.builder()
                .userId(1L)
                .studentNumber("STU001")
                .groupId(1L)
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllStudentsSuccessfully() throws Exception {
        // Arrange
        List<StudentResponse> students = Arrays.asList(studentResponse);
        when(studentService.getAllStudents()).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].studentNumber").value("STU001"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetStudentByIdSuccessfully() throws Exception {
        // Arrange
        when(studentService.getStudentById(1L)).thenReturn(studentResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/students/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.studentNumber").value("STU001"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateStudentSuccessfully() throws Exception {
        // Arrange
        when(studentService.createStudent(any(CreateStudentRequest.class))).thenReturn(studentResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createStudentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentNumber").value("STU001"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void shouldGetStudentsByGroupIdSuccessfully() throws Exception {
        // Arrange
        List<StudentResponse> students = Arrays.asList(studentResponse);
        when(studentService.getStudentsByGroupId(1L)).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/api/v1/students/group/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentNumber").value("STU001"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnBadRequestWhenCreatingStudentWithMissingUserId() throws Exception {
        // Arrange
        CreateStudentRequest invalidRequest = CreateStudentRequest.builder()
                .userId(null)
                .studentNumber("STU001")
                .groupId(1L)
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnUnauthorizedWhenAccessingWithoutToken() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}



