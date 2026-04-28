-- Migration V5: Create student_subject_absences table for tracking absences and failure status
CREATE TABLE student_subject_absences (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    absence_count INT NOT NULL DEFAULT 0,
    failed_due_to_absence BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT UNIQUE (student_id, subject_id),
    CONSTRAINT fk_ssa_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_ssa_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    INDEX idx_student_subject (student_id, subject_id),
    INDEX idx_failed_due_to_absence (failed_due_to_absence)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

