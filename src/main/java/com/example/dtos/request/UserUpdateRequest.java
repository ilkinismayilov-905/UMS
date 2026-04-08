package com.example.dtos.request;

import com.example.enums.Role;
import jakarta.validation.constraints.Email;

public record UserUpdateRequest(
        @Email(message = "Email must be valid")
        String email,

        String password,

        Role role,

        String firstName,

        String lastName,

        Boolean isActive
) {}
