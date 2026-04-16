package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStudentRequest {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotBlank(message = "Student number cannot be blank")
    private String studentNumber;

    @NotNull(message = "Group ID cannot be null")
    private Long groupId;
}

