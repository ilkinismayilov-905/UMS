package com.example.controller;

import com.example.dto.request.MarkAttendanceRequest;
import com.example.dto.response.AttendanceResponse;
import com.example.dto.response.AttendanceWarningResponse;
import com.example.service.AttendanceService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AttendanceControllerTest {

    @Mock
    private AttendanceService attendanceService;

    @InjectMocks
    private AttendanceController attendanceController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    private MarkAttendanceRequest markAttendanceRequest;
    private AttendanceResponse attendanceResponse;
    private AttendanceWarningResponse warningResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(attendanceController).build();

        LocalDateTime now = LocalDateTime.now();

        markAttendanceRequest = new MarkAttendanceRequest(
                1L,
                1L,
                "ABSENT",
                "Late arrival"
        );

        attendanceResponse = new AttendanceResponse(
                1L,
                1L,
                null,
                "ABSENT",
                now,
                now,
                "Late arrival"
        );

        warningResponse = new AttendanceWarningResponse(
                true,
                "Cannot change ABSENT to PRESENT after 15 minutes of lesson start",
                "ABSENT",
                "PRESENT",
                1L
        );
    }

    @Test
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
    void shouldReturnWarningResponseWhenRevertingAfter15Minutes() throws Exception {
        // Arrange
        MarkAttendanceRequest revertRequest = new MarkAttendanceRequest(
                1L,
                1L,
                "PRESENT",
                null
        );

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

//    @Test
//    void shouldReturnForbiddenWhenStudentTriesToMarkAttendance() throws Exception {
//        mockMvc.perform(post("/api/v1/attendance/mark")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(markAttendanceRequest)))
//                .andExpect(status().isForbidden());
//    }

    @Test
    void shouldGetStudentAttendanceSuccessfully() throws Exception {
        // Arrange
        List<AttendanceResponse> attendances = List.of(attendanceResponse);
        when(attendanceService.getStudentAttendance(1L)).thenReturn(attendances);

        // Act & Assert
        mockMvc.perform(get("/api/v1/attendance/student/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ABSENT"));
    }

    @Test
    void shouldGetLessonAttendanceSuccessfully() throws Exception {
        // Arrange
        List<AttendanceResponse> attendances = List.of(attendanceResponse);
        when(attendanceService.getLessonAttendance(1L)).thenReturn(attendances);

        // Act & Assert
        mockMvc.perform(get("/api/v1/attendance/lesson/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ABSENT"));
    }

    @Test
    void shouldReturnBadRequestWhenLessonIdIsMissing() throws Exception {
        // Arrange
        MarkAttendanceRequest invalidRequest = new MarkAttendanceRequest(
                null,
                1L,
                "ABSENT",
                null
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/attendance/mark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenStudentIdIsMissing() throws Exception {
        // Arrange
        MarkAttendanceRequest invalidRequest = new MarkAttendanceRequest(
                1L,
                null,
                "ABSENT",
                null
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/attendance/mark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenStatusIsMissing() throws Exception {
        // Arrange
        MarkAttendanceRequest invalidRequest = new MarkAttendanceRequest(
                1L,
                1L,
                "",
                null
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/attendance/mark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    public void shouldReturnUnauthorizedWhenAccessingWithoutToken() throws Exception {
//        mockMvc.perform(get("/api/v1/attendance/student/1")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isUnauthorized());
//    }
}



