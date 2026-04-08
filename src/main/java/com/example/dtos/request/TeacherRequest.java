package com.example.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TeacherRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotBlank(message = "Department is required")
        String department
) {}
