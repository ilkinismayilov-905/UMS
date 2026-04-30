package com.example.dto;

import lombok.Builder;

@Builder
public record DashboardSummaryDTO(
    long totalStudents,
    long activeStudents,
    long totalTeachers,
    long totalGroups,
    long totalDepartments,
    long totalSpecialties,
    long totalUsers,
    long totalSubjects
) {
}
