package com.example.entity;

import com.example.enums.GradeStatus;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Teacher teacher;

    @Column(nullable = false)
    private Integer attendanceScore;  // Max 10

    @Column(nullable = false)
    private Integer seminarScore;     // Max 10

    @Column(nullable = false)
    private Integer col1;             // Max 10

    @Column(nullable = false)
    private Integer col2;             // Max 10

    @Column(nullable = false)
    private Integer col3;             // Max 10

    @Column(nullable = false)
    private Integer examScore;        // Max 50

    @Column(nullable = false)
    private Integer totalScore;       // Auto-calculated

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GradeStatus status;

    @PrePersist
    @PreUpdate
    private void calculateTotal() {
        this.totalScore = (attendanceScore != null ? attendanceScore : 0)
                + (seminarScore != null ? seminarScore : 0)
                + (col1 != null ? col1 : 0)
                + (col2 != null ? col2 : 0)
                + (col3 != null ? col3 : 0)
                + (examScore != null ? examScore : 0);
    }
}
