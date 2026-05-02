package com.example.controller;

import com.example.dto.response.GroupDto;
import com.example.dto.response.SubjectDto;
import com.example.dto.response.TeacherDashboardResponse;
import com.example.security.UserDetailsImpl;
import com.example.service.TeacherDashboardService;
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
class TeacherDashboardControllerTest {

    @Mock
    private TeacherDashboardService teacherDashboardService;

    @InjectMocks
    private TeacherDashboardController teacherDashboardController;

    private MockMvc mockMvc;
    private UsernamePasswordAuthenticationToken authentication;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherDashboardController).build();

        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "teacher@school.com",
                "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_TEACHER")),
                true
        );

        authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
    }

    @Test
    void shouldGetTeacherDashboardSuccessfully() throws Exception {
        // Arrange
        TeacherDashboardResponse dashboardResponse = new TeacherDashboardResponse(
                "Jane",
                "Smith",
                "teacher@school.com",
                "COMPUTER_SCIENCE",
                List.of(new GroupDto(1L, "651.21"), new GroupDto(2L, "651.22")),
                List.of(new SubjectDto(1L, "Mathematics", 3), new SubjectDto(2L, "Physics", 4))
        );
        when(teacherDashboardService.getTeacherDashboardData(1L)).thenReturn(dashboardResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/teacher/dashboard")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("teacher@school.com"))
                .andExpect(jsonPath("$.department").value("COMPUTER_SCIENCE"))
                .andExpect(jsonPath("$.assignedGroups[0].groupNumber").value("651.21"))
                .andExpect(jsonPath("$.assignedGroups[1].groupNumber").value("651.22"))
                .andExpect(jsonPath("$.assignedSubjects[0].name").value("Mathematics"))
                .andExpect(jsonPath("$.assignedSubjects[1].name").value("Physics"));
    }
}
