package com.example.controller;

import com.example.dto.DashboardSummaryDTO;
import com.example.service.SuperAdminDashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SuperAdminDashboardControllerTest {

    @Mock
    private SuperAdminDashboardService superAdminDashboardService;

    @InjectMocks
    private SuperAdminDashboardController superAdminDashboardController;

    private MockMvc mockMvc;

    private DashboardSummaryDTO dashboardSummaryDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(superAdminDashboardController).build();

        dashboardSummaryDTO = new DashboardSummaryDTO(
                100, 85, 20, 10, 5, 8, 130, 15
        );
    }

    @Test
    void shouldGetDashboardSummarySuccessfully() throws Exception {
        // Arrange
        when(superAdminDashboardService.getDashboardSummary()).thenReturn(dashboardSummaryDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/admin/dashboard/summary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalStudents").value(100))
                .andExpect(jsonPath("$.activeStudents").value(85))
                .andExpect(jsonPath("$.totalTeachers").value(20))
                .andExpect(jsonPath("$.totalGroups").value(10))
                .andExpect(jsonPath("$.totalDepartments").value(5))
                .andExpect(jsonPath("$.totalSpecialties").value(8))
                .andExpect(jsonPath("$.totalUsers").value(130))
                .andExpect(jsonPath("$.totalSubjects").value(15));
    }
}
