package com.example.dtos.request;

import jakarta.validation.constraints.NotNull;

public record TeacherGroupSubjectRequest(
        @NotNull(message = "Teacher ID is required")
        Long teacherId,

        @NotNull(message = "Group ID is required")
        Long groupId,

        @NotNull(message = "Subject ID is required")
        Long subjectId
) {}
