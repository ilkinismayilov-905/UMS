package com.example.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record SpecialtyRequest(
        @NotBlank(message = "Specialty name is required")
        String name
) {}
