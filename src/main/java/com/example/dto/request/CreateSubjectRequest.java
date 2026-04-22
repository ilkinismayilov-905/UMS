package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CreateSubjectRequest(
    @NotBlank(message = "Subject name cannot be blank")
    String name,

    @NotNull(message = "Credits cannot be null")
    @Positive(message = "Credits must be a positive number")
    Integer credits
) {
}

