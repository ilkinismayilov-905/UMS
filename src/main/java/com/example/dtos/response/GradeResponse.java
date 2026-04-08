package com.example.dtos.response;

import com.example.enums.GradeStatus;

public record GradeResponse(
        Long id,
        StudentResponse student,
        SubjectResponse subject,
        TeacherResponse teacher,
        Integer attendanceScore,
        Integer seminarScore,
        Integer col1,
        Integer col2,
        Integer col3,
        Integer examScore,
        Integer totalScore,
        GradeStatus status
) {}
