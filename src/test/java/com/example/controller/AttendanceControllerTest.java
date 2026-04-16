package com.example.controller;

import com.example.dto.request.MarkAttendanceRequest;
import com.example.dto.response.AttendanceResponse;
import com.example.dto.response.AttendanceWarningResponse;
import com.example.service.AttendanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AttendanceController.class)
class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AttendanceService attendanceService;

    private MarkAttendanceRequest markAttendanceRequest;
    private AttendanceResponse attendanceResponse;
    private AttendanceWarningResponse warningResponse;

    @BeforeEach
    void setUp() {
        markAttendanceRequest = MarkAttendanceRequest.builder()
                .lessonId(1L)
                .studentId(1L)
                .status("ABSENT")
                .remarks("Late arrival")
                .build();

        attendanceResponse = AttendanceResponse.builder()
                .id(1L)
                .lessonId(1L)
                .status("ABSENT")
                .markedAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .remarks("Late arrival")
                .build();

        warningResponse = AttendanceWarningResponse.builder()
                .warning(true)
                .message("Cannot change ABSENT to PRESENT after 15 minutes of lesson start")
                .currentStatus("ABSENT")
                .requestedStatus("PRESENT")
                .attendanceId(1L)
                .build();
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void shouldMarkAttendanceSuccessfully() throws Exception {
        // Arrange
        when(attendanceService.markAttendance(any(MarkAttendanceRequest.class)))
                .thenReturn(attendanceResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/attendance/mark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(markAttendanceRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ABSENT"))
                .andExpect(jsonPath("$.remarks").value("Late arrival"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void shouldReturnWarningResponseWhenRevertingAfter15Minutes() throws Exception {
        // Arrange
        MarkAttendanceRequest revertRequest = MarkAttendanceRequest.builder()
                .lessonId(1L)
                .studentId(1L)
                .status("PRESENT")
                .build();

        when(attendanceService.markAttendance(any(MarkAttendanceRequest.class)))
                .thenReturn(warningResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/attendance/mark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(revertRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.warning").value(true))
                .andExpect(jsonPath("$.currentStatus").value("ABSENT"))
                .andExpect(jsonPath("$.requestedStatus").value("PRESENT"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void shouldReturnForbiddenWhenStudentTriesToMarkAttendance() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/attendance/mark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(markAttendanceRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void shouldGetStudentAttendanceSuccessfully() throws Exception {
        // Arrange
        List<AttendanceResponse> attendances = Arrays.asList(attendanceResponse);
        when(attendanceService.getStudentAttendance(1L)).thenReturn(attendances);

        // Act & Assert
        mockMvc.perform(get("/api/v1/attendance/student/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ABSENT"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void shouldGetLessonAttendanceSuccessfully() throws Exception {
        // Arrange
        List<AttendanceResponse> attendances = Arrays.asList(attendanceResponse);
        when(attendanceService.getLessonAttendance(1L)).thenReturn(attendances);

        // Act & Assert
        mockMvc.perform(get("/api/v1/attendance/lesson/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ABSENT"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void shouldReturnBadRequestWhenLessonIdIsMissing() throws Exception {
        // Arrange
        MarkAttendanceRequest invalidRequest = MarkAttendanceRequest.builder()
                .lessonId(null)
                .studentId(1L)
                .status("ABSENT")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/attendance/mark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void shouldReturnBadRequestWhenStudentIdIsMissing() throws Exception {
        // Arrange
        MarkAttendanceRequest invalidRequest = MarkAttendanceRequest.builder()
                .lessonId(1L)
                .studentId(null)
                .status("ABSENT")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/attendance/mark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void shouldReturnBadRequestWhenStatusIsMissing() throws Exception {
        // Arrange
        MarkAttendanceRequest invalidRequest = MarkAttendanceRequest.builder()
                .lessonId(1L)
                .studentId(1L)
                .status("")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/attendance/mark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnUnauthorizedWhenAccessingWithoutToken() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/attendance/student/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}



