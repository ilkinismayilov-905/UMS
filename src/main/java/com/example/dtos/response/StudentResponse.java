package com.example.dtos.response;

public record StudentResponse(
        Long id,
        String studentNumber,
        UserResponse user,
        Long groupId,
        String groupNumber
) {}
