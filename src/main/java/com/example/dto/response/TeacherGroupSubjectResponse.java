package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherGroupSubjectResponse {

    private Long id;

    private TeacherResponse teacher;

    private GroupResponse group;

    private SubjectResponse subject;
}

