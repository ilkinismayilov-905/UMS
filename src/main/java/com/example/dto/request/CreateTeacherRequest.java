package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateTeacherRequest(
    @NotNull(message = "User ID cannot be null")
    Long userId,

    @NotBlank(message = "Department cannot be blank")
    String department
) {
}

