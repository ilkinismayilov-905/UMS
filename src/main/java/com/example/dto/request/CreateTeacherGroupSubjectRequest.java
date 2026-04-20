package com.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateTeacherGroupSubjectRequest(
    @NotNull(message = "Teacher ID cannot be null")
    Long teacherId,

    @NotNull(message = "Group ID cannot be null")
    Long groupId,

    @NotNull(message = "Subject ID cannot be null")
    Long subjectId
) {
}

