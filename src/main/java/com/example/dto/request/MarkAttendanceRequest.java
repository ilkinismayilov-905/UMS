package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkAttendanceRequest {

    @NotNull(message = "Lesson ID cannot be null")
    private Long lessonId;

    @NotNull(message = "Student ID cannot be null")
    private Long studentId;

    @NotBlank(message = "Status cannot be blank (PRESENT, ABSENT)")
    private String status;

    private String remarks;
}

