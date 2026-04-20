package com.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateStudentRequest(
    @NotNull(message = "Group ID cannot be null")
    Long groupId
) {
}

