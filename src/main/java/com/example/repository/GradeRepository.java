package com.example.repository;

import com.example.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findByStudentIdAndSubjectId(Long studentId, Long subjectId);

    List<Grade> findAllByStudentId(Long studentId);

    List<Grade> findAllByTeacherId(Long teacherId);

    /**
     * Fetch all grades for a student with subject information to avoid N+1 queries
     */
    @Query("SELECT g FROM Grade g " +
           "LEFT JOIN FETCH g.subject s " +
           "WHERE g.student.id = :studentId " +
           "ORDER BY s.id ASC")
    List<Grade> findAllByStudentIdWithSubjectDetails(@Param("studentId") Long studentId);
}
