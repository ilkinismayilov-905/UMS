package com.example.service;

import com.example.entity.Student;
import com.example.entity.StudentSubjectAbsence;
import com.example.entity.Subject;
import com.example.repository.AttendanceRepository;
import com.example.repository.StudentSubjectAbsenceRepository;
import com.example.strategy.AbsenceLimitStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceTrackingService {

    private final StudentSubjectAbsenceRepository studentSubjectAbsenceRepository;
    private final AttendanceRepository attendanceRepository;

    /**
     * Track absence for a student in a subject
     * Updates absence count and checks if student should fail due to absences
     */
    @Transactional
    public StudentSubjectAbsence trackAbsence(Student student, Subject subject) {
        log.info("Tracking absence for student id: {} in subject id: {}", student.getId(), subject.getId());

        StudentSubjectAbsence studentSubjectAbsence = studentSubjectAbsenceRepository
                .findByStudentAndSubject(student, subject)
                .orElse(StudentSubjectAbsence.builder()
                        .student(student)
                        .subject(subject)
                        .absenceCount(0)
                        .failedDueToAbsence(false)
                        .build());

        // Increment absence count
        studentSubjectAbsence.setAbsenceCount(studentSubjectAbsence.getAbsenceCount() + 1);
        log.debug("Updated absence count for student id: {} in subject id: {} to: {}",
                student.getId(), subject.getId(), studentSubjectAbsence.getAbsenceCount());

        // Check if limit is exceeded (student fails at limit + 1)
        if (AbsenceLimitStrategy.hasExceededLimit(studentSubjectAbsence.getAbsenceCount(), subject.getAbsenceLimit())) {
            studentSubjectAbsence.setFailedDueToAbsence(true);
            log.warn("Student id: {} has exceeded absence limit for subject id: {}. Marking as failed.",
                    student.getId(), subject.getId());
        }

        return studentSubjectAbsenceRepository.save(studentSubjectAbsence);
    }

    /**
     * Check if student has failed due to absences in a subject
     */
    @Transactional(readOnly = true)
    public boolean hasStudentFailedDueToAbsence(Student student, Subject subject) {
        return studentSubjectAbsenceRepository.findByStudentAndSubject(student, subject)
                .map(StudentSubjectAbsence::getFailedDueToAbsence)
                .orElse(false);
    }

    /**
     * Get absence count for a student in a subject
     */
    @Transactional(readOnly = true)
    public int getAbsenceCount(Student student, Subject subject) {
        return studentSubjectAbsenceRepository.findByStudentAndSubject(student, subject)
                .map(StudentSubjectAbsence::getAbsenceCount)
                .orElse(0);
    }

    /**
     * Get StudentSubjectAbsence record, creating if doesn't exist
     */
    @Transactional
    public StudentSubjectAbsence getOrCreateStudentSubjectAbsence(Student student, Subject subject) {
        return studentSubjectAbsenceRepository.findByStudentAndSubject(student, subject)
                .orElseGet(() -> studentSubjectAbsenceRepository.save(
                        StudentSubjectAbsence.builder()
                                .student(student)
                                .subject(subject)
                                .absenceCount(0)
                                .failedDueToAbsence(false)
                                .build()
                ));
    }
}

