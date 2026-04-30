package com.example.dto.request;

import com.example.enums.Department;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateTeacherRequest(
    @NotNull(message = "Department cannot be null")
    Department department
) {
}
