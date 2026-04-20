package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateTeacherRequest(
    @NotBlank(message = "Department cannot be blank")
    String department
) {
}

