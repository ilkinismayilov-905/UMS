package com.example.dtos.request;

import com.example.enums.GradeStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record GradeRequest(
        @NotNull(message = "Student ID is required")
        Long studentId,

        @NotNull(message = "Subject ID is required")
        Long subjectId,

        @NotNull(message = "Teacher ID is required")
        Long teacherId,

        @NotNull(message = "Attendance score is required")
        @Min(value = 0, message = "Attendance score must be at least 0")
        @Max(value = 10, message = "Attendance score must be at most 10")
        Integer attendanceScore,

        @NotNull(message = "Seminar score is required")
        @Min(value = 0, message = "Seminar score must be at least 0")
        @Max(value = 10, message = "Seminar score must be at most 10")
        Integer seminarScore,

        @NotNull(message = "Col1 score is required")
        @Min(value = 0, message = "Col1 must be at least 0")
        @Max(value = 10, message = "Col1 must be at most 10")
        Integer col1,

        @NotNull(message = "Col2 score is required")
        @Min(value = 0, message = "Col2 must be at least 0")
        @Max(value = 10, message = "Col2 must be at most 10")
        Integer col2,

        @NotNull(message = "Col3 score is required")
        @Min(value = 0, message = "Col3 must be at least 0")
        @Max(value = 10, message = "Col3 must be at most 10")
        Integer col3,

        @NotNull(message = "Exam score is required")
        @Min(value = 0, message = "Exam score must be at least 0")
        @Max(value = 50, message = "Exam score must be at most 50")
        Integer examScore,

        @NotNull(message = "Status is required")
        GradeStatus status
) {}
