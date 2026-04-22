package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateGroupRequest(
    @NotBlank(message = "Group number cannot be blank")
    String groupNumber,

    @NotNull(message = "Specialty ID cannot be null")
    Long specialtyId
) {
}

