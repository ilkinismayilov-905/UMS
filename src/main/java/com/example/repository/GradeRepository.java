package com.example.repository;

import com.example.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findByStudentIdAndSubjectId(Long studentId, Long subjectId);

    List<Grade> findAllByStudentId(Long studentId);

    List<Grade> findAllByTeacherId(Long teacherId);
}
