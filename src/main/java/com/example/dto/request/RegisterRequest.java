package com.example.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RegisterRequest(
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    String email,

    @NotBlank(message = "Password cannot be blank")
    String password,

    @NotBlank(message = "First name cannot be blank")
    String firstName,

    @NotBlank(message = "Last name cannot be blank")
    String lastName,

    @NotBlank(message = "Role cannot be blank")
    String role
) {
}

