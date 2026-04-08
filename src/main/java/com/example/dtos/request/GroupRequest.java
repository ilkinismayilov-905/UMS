package com.example.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GroupRequest(
        @NotBlank(message = "Group number is required")
        String groupNumber,

        @NotNull(message = "Specialty ID is required")
        Long specialtyId
) {}
