package com.example.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubjectRequest(
        @NotBlank(message = "Subject name is required")
        String name,

        @NotNull(message = "Credits is required")
        @Min(value = 1, message = "Credits must be at least 1")
        Integer credits
) {}
