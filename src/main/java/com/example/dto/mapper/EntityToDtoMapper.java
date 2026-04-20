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
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name(),
                user.getIsActive()
        );
    }

    public StudentResponse toStudentResponse(Student student) {
        if (student == null) {
            return null;
        }
        return new StudentResponse(
                student.getId(),
                student.getStudentNumber(),
                toUserResponse(student.getUser()),
                toGroupResponse(student.getGroup())
        );
    }

    public TeacherResponse toTeacherResponse(Teacher teacher) {
        if (teacher == null) {
            return null;
        }
        return new TeacherResponse(
                teacher.getId(),
                toUserResponse(teacher.getUser()),
                teacher.getDepartment()
        );
    }

    public GroupResponse toGroupResponse(Group group) {
        if (group == null) {
            return null;
        }
        return new GroupResponse(
                group.getId(),
                group.getGroupNumber(),
                toSpecialtyResponse(group.getSpecialty())
        );
    }

    public SpecialtyResponse toSpecialtyResponse(Specialty specialty) {
        if (specialty == null) {
            return null;
        }
        return new SpecialtyResponse(
                specialty.getId(),
                specialty.getName()
        );
    }

    public SubjectResponse toSubjectResponse(Subject subject) {
        if (subject == null) {
            return null;
        }
        return new SubjectResponse(
                subject.getId(),
                subject.getName(),
                subject.getCredits()
        );
    }

    public GradeResponse toGradeResponse(Grade grade) {
        if (grade == null) {
            return null;
        }
        return new GradeResponse(
                grade.getId(),
                toStudentResponse(grade.getStudent()),
                toSubjectResponse(grade.getSubject()),
                toTeacherResponse(grade.getTeacher()),
                grade.getAttendanceScore(),
                grade.getSeminarScore(),
                grade.getCol1(),
                grade.getCol2(),
                grade.getCol3(),
                grade.getExamScore(),
                grade.getTotalScore(),
                grade.getStatus() != null ? grade.getStatus().name() : null
        );
    }

    public TeacherGroupSubjectResponse toTeacherGroupSubjectResponse(TeacherGroupSubject tgs) {
        if (tgs == null) {
            return null;
        }
        return new TeacherGroupSubjectResponse(
                tgs.getId(),
                toTeacherResponse(tgs.getTeacher()),
                toGroupResponse(tgs.getGroup()),
                toSubjectResponse(tgs.getSubject())
        );
    }

    public AttendanceResponse toAttendanceResponse(Attendance attendance) {
        if (attendance == null) {
            return null;
        }
        return new AttendanceResponse(
                attendance.getId(),
                attendance.getLesson().getId(),
                toStudentResponse(attendance.getStudent()),
                attendance.getStatus().name(),
                attendance.getMarkedAt(),
                attendance.getLastModifiedAt(),
                attendance.getRemarks()
        );
    }
}

