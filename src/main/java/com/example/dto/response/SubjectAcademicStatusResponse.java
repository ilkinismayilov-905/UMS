package com.example.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record SubjectAcademicStatusResponse(
    Long subjectId,
    String subjectName,
    Integer credits,
    List<Integer> grades,
    Integer totalAbsences
) {
}

