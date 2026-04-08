package com.example.dtos.response;

public record TeacherResponse(
        Long id,
        UserResponse user,
        String department
) {}
