package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceWarningResponse {

    private boolean warning;
    private String message;
    private String currentStatus;
    private String requestedStatus;
    private Long attendanceId;
}

