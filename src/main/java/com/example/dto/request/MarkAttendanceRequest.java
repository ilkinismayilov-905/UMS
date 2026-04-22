package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MarkAttendanceRequest(
    @NotNull(message = "Lesson ID cannot be null")
    Long lessonId,

    @NotNull(message = "Student ID cannot be null")
    Long studentId,

    @NotBlank(message = "Status cannot be blank (PRESENT, ABSENT)")
    String status,

    String remarks
) {
}

