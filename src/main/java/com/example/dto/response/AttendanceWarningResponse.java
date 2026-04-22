package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public record AttendanceWarningResponse(
        boolean warning,
        String message,
        String currentStatus,
        String requestedStatus,
        Long attendanceId
) {
}
