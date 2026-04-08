package com.example.dtos.response;

public record TeacherGroupSubjectResponse(
        Long id,
        TeacherResponse teacher,
        GroupResponse group,
        SubjectResponse subject
) {}
