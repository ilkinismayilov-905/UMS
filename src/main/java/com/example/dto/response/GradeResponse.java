package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeResponse {

    private Long id;

    private Long studentId;

    private Long subjectId;

    private Long teacherId;

    private Integer attendanceScore;

    private Integer seminarScore;

    private Integer col1;

    private Integer col2;

    private Integer col3;

    private Integer examScore;

    private Integer totalScore;

    private String status;
}

