package com.example.dto.response;

import lombok.Builder;

@Builder
public record EnrolledSubjectResponse(
    Long id,
    String name,
    Integer credits,
    Integer absenceLimit
) {
}

