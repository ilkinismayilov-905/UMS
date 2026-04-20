package com.example.controller;

import com.example.dto.request.CreateStudentRequest;
import com.example.dto.response.StudentResponse;
import com.example.dto.response.UserResponse;
import com.example.service.StudentService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    private StudentResponse studentResponse;
    private CreateStudentRequest createStudentRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();

        UserResponse userResponse = new UserResponse(
                1L,
                "student@school.com",
                "John",
                "Doe",
                "STUDENT",
                true
        );

        studentResponse = new StudentResponse(
                1L,
                "STU001",
                userResponse,
                null
        );

        createStudentRequest = new CreateStudentRequest(
                1L,
                "STU001",
                1L
        );
    }

    @Test
    void shouldGetAllStudentsSuccessfully() throws Exception {
        // Arrange
        List<StudentResponse> students = List.of(studentResponse);
        when(studentService.getAllStudents()).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].studentNumber").value("STU001"));
    }

    @Test
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
    void shouldGetStudentsByGroupIdSuccessfully() throws Exception {
        // Arrange
        List<StudentResponse> students = List.of(studentResponse);
        when(studentService.getStudentsByGroupId(1L)).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/api/v1/students/group/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentNumber").value("STU001"));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingStudentWithMissingUserId() throws Exception {
        // Arrange
        CreateStudentRequest invalidRequest = new CreateStudentRequest(
                null,
                "STU001",
                1L
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    public void shouldReturnUnauthorizedWhenAccessingWithoutToken() throws Exception {
//        // Act & Assert
//        mockMvc.perform(get("/api/v1/students")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isUnauthorized());
//    }
}



