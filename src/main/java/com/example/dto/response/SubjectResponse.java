package com.example.dto.response;

import lombok.Builder;

@Builder
public record SubjectResponse(
    Long id,
    String name,
    Integer credits
) {
}

