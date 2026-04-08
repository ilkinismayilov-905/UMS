package com.example.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotBlank(message = "Student number is required")
        String studentNumber,

        @NotNull(message = "Group ID is required")
        Long groupId
) {}
