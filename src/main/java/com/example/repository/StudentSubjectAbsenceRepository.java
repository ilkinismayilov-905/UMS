package com.example.repository;

import com.example.entity.Student;
import com.example.entity.StudentSubjectAbsence;
import com.example.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentSubjectAbsenceRepository extends JpaRepository<StudentSubjectAbsence, Long> {

    Optional<StudentSubjectAbsence> findByStudentAndSubject(Student student, Subject subject);

    boolean existsByStudentAndSubject(Student student, Subject subject);
}

