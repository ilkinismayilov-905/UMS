package com.example.dto.response;

import lombok.Builder;

@Builder
public record TeacherResponse(
    Long id,
    UserResponse user,
    String department
) {
}

