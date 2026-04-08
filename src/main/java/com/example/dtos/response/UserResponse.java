package com.example.dtos.response;

import com.example.enums.Role;

public record UserResponse(
        Long id,
        String email,
        Role role,
        String firstName,
        String lastName,
        Boolean isActive
) {}
