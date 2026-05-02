package com.example.controller;

import com.example.dto.response.EnrolledSubjectResponse;
import com.example.dto.response.StudentProfileResponse;
import com.example.dto.response.SubjectAcademicStatusResponse;
import com.example.security.UserDetailsImpl;
import com.example.service.StudentDashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentDashboardControllerTest {

    @Mock
    private StudentDashboardService studentDashboardService;

    @InjectMocks
    private StudentDashboardController studentDashboardController;

    private MockMvc mockMvc;
    private UsernamePasswordAuthenticationToken authentication;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentDashboardController).build();

        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "student@school.com",
                "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_STUDENT")),
                true
        );

        authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
    }

    @Test
    void shouldGetStudentProfileSuccessfully() throws Exception {
        // Arrange
        StudentProfileResponse profile = new StudentProfileResponse(
                "John", "Doe", "student@school.com", "651.21", "STU001", "Computer Science"
        );
        when(studentDashboardService.getStudentProfile(1L)).thenReturn(profile);

        // Act & Assert
        mockMvc.perform(get("/api/v1/dashboard/student/profile")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("student@school.com"))
                .andExpect(jsonPath("$.groupNumber").value("651.21"))
                .andExpect(jsonPath("$.studentNumber").value("STU001"))
                .andExpect(jsonPath("$.specialty").value("Computer Science"));
    }

    @Test
    void shouldGetStudentAcademicStatusSuccessfully() throws Exception {
        // Arrange
        SubjectAcademicStatusResponse status = new SubjectAcademicStatusResponse(
                1L, "Mathematics", 3, List.of(8, 7, 9), 2
        );
        when(studentDashboardService.getStudentAcademicStatus(1L)).thenReturn(List.of(status));

        // Act & Assert
        mockMvc.perform(get("/api/v1/dashboard/student/academic-status")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].subjectName").value("Mathematics"))
                .andExpect(jsonPath("$[0].credits").value(3))
                .andExpect(jsonPath("$[0].totalAbsences").value(2));
    }

    @Test
    void shouldGetStudentEnrolledSubjectsSuccessfully() throws Exception {
        // Arrange
        EnrolledSubjectResponse subject = new EnrolledSubjectResponse(1L, "Mathematics", 3, 10);
        when(studentDashboardService.getStudentEnrolledSubjects(1L)).thenReturn(List.of(subject));

        // Act & Assert
        mockMvc.perform(get("/api/v1/dashboard/student/subjects")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Mathematics"))
                .andExpect(jsonPath("$[0].credits").value(3))
                .andExpect(jsonPath("$[0].absenceLimit").value(10));
    }
}
