package com.example.dto.mapper;

import com.example.dto.response.StudentProfileResponse;
import com.example.dto.response.EnrolledSubjectResponse;
import com.example.entity.Student;
import com.example.entity.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentDashboardMapper {

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "groupNumber", source = "group.groupNumber")
    @Mapping(target = "specialty", source = "group.specialty.name")
    StudentProfileResponse toStudentProfileResponse(Student student);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "credits", source = "credits")
    @Mapping(target = "absenceLimit", source = "absenceLimit")
    EnrolledSubjectResponse toEnrolledSubjectResponse(Subject subject);
}


