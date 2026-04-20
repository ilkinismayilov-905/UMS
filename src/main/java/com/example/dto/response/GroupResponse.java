package com.example.dto.response;

import lombok.Builder;

@Builder
public record GroupResponse(
    Long id,
    String groupNumber,
    SpecialtyResponse specialty
) {
}

