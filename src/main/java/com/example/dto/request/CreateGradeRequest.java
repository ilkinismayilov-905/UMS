package com.example.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateGradeRequest(
    @NotNull(message = "Student ID cannot be null")
    Long studentId,

    @NotNull(message = "Subject ID cannot be null")
    Long subjectId,

    @NotNull(message = "Teacher ID cannot be null")
    Long teacherId,

    @Min(value = 0, message = "Attendance score must be at least 0")
    @Max(value = 10, message = "Attendance score must not exceed 10")
    Integer attendanceScore,

    @Min(value = 0, message = "Seminar score must be at least 0")
    @Max(value = 10, message = "Seminar score must not exceed 10")
    Integer seminarScore,

    @Min(value = 0, message = "Colloquium 1 score must be at least 0")
    @Max(value = 10, message = "Colloquium 1 score must not exceed 10")
    Integer col1,

    @Min(value = 0, message = "Colloquium 2 score must be at least 0")
    @Max(value = 10, message = "Colloquium 2 score must not exceed 10")
    Integer col2,

    @Min(value = 0, message = "Colloquium 3 score must be at least 0")
    @Max(value = 10, message = "Colloquium 3 score must not exceed 10")
    Integer col3,

    @Min(value = 0, message = "Exam score must be at least 0")
    @Max(value = 50, message = "Exam score must not exceed 50")
    Integer examScore
) {
}

