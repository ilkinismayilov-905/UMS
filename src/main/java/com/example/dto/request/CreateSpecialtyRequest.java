package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateSpecialtyRequest(
    @NotBlank(message = "Specialty name cannot be blank")
    String name
) {
}

