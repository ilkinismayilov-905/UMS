package com.example.dto.response;

import lombok.Builder;

@Builder
public record StudentProfileResponse(
    String firstName,
    String lastName,
    String email,
    String groupNumber,
    String studentNumber,
    String specialty
) {
}

