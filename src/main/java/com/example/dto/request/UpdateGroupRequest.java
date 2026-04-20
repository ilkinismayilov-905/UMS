package com.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateGroupRequest(
    @NotNull(message = "Specialty ID cannot be null")
    Long specialtyId
) {
}

