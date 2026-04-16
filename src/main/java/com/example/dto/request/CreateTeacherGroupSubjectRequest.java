package com.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTeacherGroupSubjectRequest {

    @NotNull(message = "Teacher ID cannot be null")
    private Long teacherId;

    @NotNull(message = "Group ID cannot be null")
    private Long groupId;

    @NotNull(message = "Subject ID cannot be null")
    private Long subjectId;
}

