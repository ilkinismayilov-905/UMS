package com.example.dto.response;

import com.example.enums.Department;
import lombok.Builder;

@Builder
public record TeacherResponse(
    Long id,
    UserResponse user,
    Department department
) {
}
