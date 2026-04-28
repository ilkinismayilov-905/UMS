package com.example.entity;

import com.example.strategy.AbsenceLimitStrategy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer credits;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 30")
    private Integer weeklyHours = 30;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 3")
    private Integer absenceLimit = 3;

    @PrePersist
    @PreUpdate
    protected void calculateAbsenceLimit() {
        if (weeklyHours != null) {
            this.absenceLimit = AbsenceLimitStrategy.calculateAbsenceLimit(weeklyHours);
        }
    }
}
