package com.example.dto.response;

import lombok.Builder;

@Builder
public record UserResponse(
    Long id,
    String email,
    String firstName,
    String lastName,
    String role,
    Boolean isActive
) {
}

