package com.example.controller;

import com.example.dto.DashboardSummaryDTO;
import com.example.service.SuperAdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
public class SuperAdminDashboardController {

    private final SuperAdminDashboardService superAdminDashboardService;

    @GetMapping("/summary")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
        DashboardSummaryDTO summary = superAdminDashboardService.getDashboardSummary();
        return ResponseEntity.ok(summary);
    }
}
