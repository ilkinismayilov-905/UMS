package com.example.controller;

import com.example.dto.response.EnrolledSubjectResponse;
import com.example.dto.response.StudentProfileResponse;
import com.example.dto.response.SubjectAcademicStatusResponse;
import com.example.security.UserDetailsImpl;
import com.example.service.StudentDashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard/student")
@RequiredArgsConstructor
@Slf4j
public class StudentDashboardController {

    private final StudentDashboardService studentDashboardService;

    /**
     * Get the authenticated student's profile information
     *
     * Endpoint: GET /api/dashboard/student/profile
     * Security: STUDENT role required
     * Returns: Student's first name, last name, email, group number, student number, and major/specialty
     *
     * @param authentication Spring Security authentication containing the authenticated user
     * @return StudentProfileResponse with student profile information
     */
    @GetMapping("/profile")
    public ResponseEntity<StudentProfileResponse> getStudentProfile(Authentication authentication) {
        log.info("GET /api/dashboard/student/profile endpoint called");

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        assert userDetails != null;
        Long userId = userDetails.getId();

        StudentProfileResponse profile = studentDashboardService.getStudentProfile(userId);
        log.info("Successfully retrieved profile for user ID: {}", userId);

        return ResponseEntity.ok(profile);
    }

    /**
     * Get the authenticated student's academic status (grades and absences by subject)
     *
     * Endpoint: GET /api/dashboard/student/academic-status
     * Security: STUDENT role required
     * Returns: List where each item contains subject name, list of grades, and total absences
     *
     * @param authentication Spring Security authentication containing the authenticated user
     * @return List of SubjectAcademicStatusResponse with subject name, grades, and absences
     */
    @GetMapping("/academic-status")
    public ResponseEntity<List<SubjectAcademicStatusResponse>> getStudentAcademicStatus(Authentication authentication) {
        log.info("GET /api/dashboard/student/academic-status endpoint called");

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        assert userDetails != null;
        Long userId = userDetails.getId();

        List<SubjectAcademicStatusResponse> academicStatus = studentDashboardService.getStudentAcademicStatus(userId);
        log.info("Successfully retrieved academic status for user ID: {} with {} subjects", userId, academicStatus.size());

        return ResponseEntity.ok(academicStatus);
    }

    /**
     * Get the authenticated student's enrolled subjects
     *
     * Endpoint: GET /api/dashboard/student/subjects
     * Security: STUDENT role required
     * Returns: List of subjects the student is currently enrolled in
     *
     * @param authentication Spring Security authentication containing the authenticated user
     * @return List of EnrolledSubjectResponse containing enrolled subjects
     */
    @GetMapping("/subjects")
    public ResponseEntity<List<EnrolledSubjectResponse>> getStudentEnrolledSubjects(Authentication authentication) {
        log.info("GET /api/dashboard/student/subjects endpoint called");

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        assert userDetails != null;
        Long userId = userDetails.getId();

        List<EnrolledSubjectResponse> enrolledSubjects = studentDashboardService.getStudentEnrolledSubjects(userId);
        log.info("Successfully retrieved {} enrolled subjects for user ID: {}", enrolledSubjects.size(), userId);

        return ResponseEntity.ok(enrolledSubjects);
    }
}




