package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSubjectRequest {

    @NotBlank(message = "Subject name cannot be blank")
    private String name;

    @NotNull(message = "Credits cannot be null")
    @Positive(message = "Credits must be a positive number")
    private Integer credits;
}

