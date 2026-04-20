package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateStudentRequest(
    @NotNull(message = "User ID cannot be null")
    Long userId,

    @NotBlank(message = "Student number cannot be blank")
    String studentNumber,

    @NotNull(message = "Group ID cannot be null")
    Long groupId
) {
}

