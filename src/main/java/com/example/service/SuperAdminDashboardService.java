package com.example.service;

import com.example.dto.DashboardSummaryDTO;
import com.example.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SuperAdminDashboardService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final SpecialtyRepository specialtyRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    @Transactional(readOnly = true)
    public DashboardSummaryDTO getDashboardSummary() {
        long totalStudents = studentRepository.countTotalStudents();
        long activeStudents = studentRepository.countActiveStudents();
        long totalTeachers = teacherRepository.countTotalTeachers();
        long totalGroups = groupRepository.count();
        long totalDepartments = teacherRepository.countDistinctDepartments();
        long totalSpecialties = specialtyRepository.count();
        long totalUsers = userRepository.count();
        long totalSubjects = subjectRepository.count();

        return DashboardSummaryDTO.builder()
                .totalStudents(totalStudents)
                .activeStudents(activeStudents)
                .totalTeachers(totalTeachers)
                .totalGroups(totalGroups)
                .totalDepartments(totalDepartments)
                .totalSpecialties(totalSpecialties)
                .totalUsers(totalUsers)
                .totalSubjects(totalSubjects)
                .build();
    }
}
