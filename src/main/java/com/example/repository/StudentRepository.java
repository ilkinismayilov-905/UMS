package com.example.repository;

import com.example.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUserId(Long userId);

    Optional<Student> findByStudentNumber(String studentNumber);

    boolean existsByStudentNumber(String studentNumber);

    List<Student> findAllByGroupId(Long groupId);

    /**
     * Fetch student with user and group information using EntityGraph pattern
     // to avoid N+1 queries
     */
    @Query("SELECT s FROM Student s " +
           "LEFT JOIN FETCH s.user u " +
           "LEFT JOIN FETCH s.group g " +
           "LEFT JOIN FETCH g.specialty sp " +
           "WHERE s.user.id = :userId")
    Optional<Student> findByUserIdWithDetails(@Param("userId") Long userId);

    @Query("SELECT COUNT(s) FROM Student s")
    long countTotalStudents();

    @Query("SELECT COUNT(s) FROM Student s WHERE s.user.isActive = true")
    long countActiveStudents();
}
