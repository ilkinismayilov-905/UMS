package com.example.service;

import com.example.dto.mapper.StudentDashboardMapper;
import com.example.dto.response.EnrolledSubjectResponse;
import com.example.dto.response.StudentProfileResponse;
import com.example.dto.response.SubjectAcademicStatusResponse;
import com.example.entity.Grade;
import com.example.entity.Student;
import com.example.entity.Subject;
import com.example.entity.StudentSubjectAbsence;
import com.example.exception.StudentNotFoundException;
import com.example.repository.GradeRepository;
import com.example.repository.StudentRepository;
import com.example.repository.StudentSubjectAbsenceRepository;
import com.example.repository.TeacherGroupSubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentDashboardService {

    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final StudentSubjectAbsenceRepository studentSubjectAbsenceRepository;
    private final TeacherGroupSubjectRepository teacherGroupSubjectRepository;
    private final StudentDashboardMapper mapper;

    /**
     * Get the authenticated student's profile information
     *
     * @param userId The authenticated user's ID
     * @return StudentProfileResponse containing student details
     * @throws StudentNotFoundException if student not found
     */
    @Transactional(readOnly = true)
    public StudentProfileResponse getStudentProfile(Long userId) {
        log.info("Fetching profile for user ID: {}", userId);

        Student student = studentRepository.findByUserIdWithDetails(userId)
                .orElseThrow(() -> {
                    log.warn("Student not found for user ID: {}", userId);
                    return new StudentNotFoundException("Student profile not found");
                });

        StudentProfileResponse response = mapper.toStudentProfileResponse(student);
        log.info("Successfully retrieved student profile for user ID: {}", userId);
        return response;
    }

    /**
     * Get the authenticated student's academic status (grades and absences by subject)
     *
     * @param userId The authenticated user's ID
     * @return List of SubjectAcademicStatusResponse containing grades and absences per subject
     * @throws StudentNotFoundException if student not found
     */
    @Transactional(readOnly = true)
    public List<SubjectAcademicStatusResponse> getStudentAcademicStatus(Long userId) {
        log.info("Fetching academic status for user ID: {}", userId);

        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Student not found for user ID: {}", userId);
                    return new StudentNotFoundException("Student not found");
                });

        // Fetch all grades for the student with subject details (optimized query)
        List<Grade> grades = gradeRepository.findAllByStudentIdWithSubjectDetails(student.getId());

        // Group grades by subject
        Map<Subject, List<Grade>> gradesBySubject = grades.stream()
                .collect(Collectors.groupingBy(Grade::getSubject));

        // Build response
        List<SubjectAcademicStatusResponse> academicStatusList = gradesBySubject.entrySet()
                .stream()
                .map(entry -> {
                    Subject subject = entry.getKey();
                    List<Grade> subjectGrades = entry.getValue();

                    // Extract total scores from grades
                    List<Integer> scoresList = subjectGrades.stream()
                            .map(Grade::getTotalScore)
                            .collect(Collectors.toList());

                    // Get absence count for this subject
                    Optional<StudentSubjectAbsence> absence = studentSubjectAbsenceRepository
                            .findByStudentAndSubject(student, subject);

                    Integer absenceCount = absence.map(StudentSubjectAbsence::getAbsenceCount)
                            .orElse(0);

                    return SubjectAcademicStatusResponse.builder()
                            .subjectId(subject.getId())
                            .subjectName(subject.getName())
                            .credits(subject.getCredits())
                            .grades(scoresList)
                            .totalAbsences(absenceCount)
                            .build();
                })
                .collect(Collectors.toList());

        log.info("Successfully retrieved academic status for user ID: {} with {} subjects", 
                userId, academicStatusList.size());
        return academicStatusList;
    }

    /**
     * Get the authenticated student's enrolled subjects
     *
     * @param userId The authenticated user's ID
     * @return List of EnrolledSubjectResponse containing subjects the student is enrolled in
     * @throws StudentNotFoundException if student not found
     */
    @Transactional(readOnly = true)
    public List<EnrolledSubjectResponse> getStudentEnrolledSubjects(Long userId) {
        log.info("Fetching enrolled subjects for user ID: {}", userId);

        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Student not found for user ID: {}", userId);
                    return new StudentNotFoundException("Student not found");
                });

        // Get student's group ID
        Long groupId = student.getGroup().getId();

        // Fetch all distinct subjects for the student's group (optimized query)
        List<Subject> enrolledSubjects = teacherGroupSubjectRepository
                .findDistinctSubjectsByGroupIdWithDetails(groupId);

        List<EnrolledSubjectResponse> subjectResponses = enrolledSubjects.stream()
                .map(mapper::toEnrolledSubjectResponse)
                .collect(Collectors.toList());

        log.info("Successfully retrieved {} enrolled subjects for user ID: {}", 
                subjectResponses.size(), userId);
        return subjectResponses;
    }
}

