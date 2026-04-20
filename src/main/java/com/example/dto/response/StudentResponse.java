package com.example.dto.response;

import lombok.Builder;

@Builder
public record StudentResponse(
    Long id,
    String studentNumber,
    UserResponse user,
    GroupResponse group
) {
}

