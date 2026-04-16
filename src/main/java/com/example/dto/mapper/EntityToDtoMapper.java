package com.example.dto.mapper;

import com.example.dto.response.*;
import com.example.entity.*;
import org.springframework.stereotype.Component;

@Component
public class EntityToDtoMapper {

    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .isActive(user.getIsActive())
                .build();
    }

    public StudentResponse toStudentResponse(Student student) {
        if (student == null) {
            return null;
        }
        return StudentResponse.builder()
                .id(student.getId())
                .studentNumber(student.getStudentNumber())
                .user(toUserResponse(student.getUser()))
                .group(toGroupResponse(student.getGroup()))
                .build();
    }

    public TeacherResponse toTeacherResponse(Teacher teacher) {
        if (teacher == null) {
            return null;
        }
        return TeacherResponse.builder()
                .id(teacher.getId())
                .user(toUserResponse(teacher.getUser()))
                .department(teacher.getDepartment())
                .build();
    }

    public GroupResponse toGroupResponse(Group group) {
        if (group == null) {
            return null;
        }
        return GroupResponse.builder()
                .id(group.getId())
                .groupNumber(group.getGroupNumber())
                .specialty(toSpecialtyResponse(group.getSpecialty()))
                .build();
    }

    public SpecialtyResponse toSpecialtyResponse(Specialty specialty) {
        if (specialty == null) {
            return null;
        }
        return SpecialtyResponse.builder()
                .id(specialty.getId())
                .name(specialty.getName())
                .build();
    }

    public SubjectResponse toSubjectResponse(Subject subject) {
        if (subject == null) {
            return null;
        }
        return SubjectResponse.builder()
                .id(subject.getId())
                .name(subject.getName())
                .credits(subject.getCredits())
                .build();
    }

    public GradeResponse toGradeResponse(Grade grade) {
        if (grade == null) {
            return null;
        }
        return GradeResponse.builder()
                .id(grade.getId())
                .student(toStudentResponse(grade.getStudent()))
                .subject(toSubjectResponse(grade.getSubject()))
                .teacher(toTeacherResponse(grade.getTeacher()))
                .attendanceScore(grade.getAttendanceScore())
                .seminarScore(grade.getSeminarScore())
                .col1(grade.getCol1())
                .col2(grade.getCol2())
                .col3(grade.getCol3())
                .examScore(grade.getExamScore())
                .totalScore(grade.getTotalScore())
                .status(grade.getStatus() != null ? grade.getStatus().name() : null)
                .build();
    }

    public TeacherGroupSubjectResponse toTeacherGroupSubjectResponse(TeacherGroupSubject tgs) {
        if (tgs == null) {
            return null;
        }
        return TeacherGroupSubjectResponse.builder()
                .id(tgs.getId())
                .teacher(toTeacherResponse(tgs.getTeacher()))
                .group(toGroupResponse(tgs.getGroup()))
                .subject(toSubjectResponse(tgs.getSubject()))
                .build();
    }

    public AttendanceResponse toAttendanceResponse(Attendance attendance) {
        if (attendance == null) {
            return null;
        }
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .lessonId(attendance.getLesson().getId())
                .student(toStudentResponse(attendance.getStudent()))
                .status(attendance.getStatus().name())
                .markedAt(attendance.getMarkedAt())
                .lastModifiedAt(attendance.getLastModifiedAt())
                .remarks(attendance.getRemarks())
                .build();
    }
}

