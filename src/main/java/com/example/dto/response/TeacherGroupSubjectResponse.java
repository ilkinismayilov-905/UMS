package com.example.dto.response;

public record TeacherGroupSubjectResponse(
    Long id,
    TeacherResponse teacher,
    GroupResponse group,
    SubjectResponse subject
) {
}

