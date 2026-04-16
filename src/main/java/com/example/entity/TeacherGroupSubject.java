package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "teacher_group_subjects",
        uniqueConstraints = @UniqueConstraint(columnNames = {"teacher_id", "group_id", "subject_id"}))
public class TeacherGroupSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Subject subject;
}
