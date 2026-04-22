package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateSpecialtyRequest(
    @NotBlank(message = "Specialty name cannot be blank")
    String name
) {
}

