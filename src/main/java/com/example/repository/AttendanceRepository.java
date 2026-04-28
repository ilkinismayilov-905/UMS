package com.example.repository;

import com.example.entity.Attendance;
import com.example.entity.Lesson;
import com.example.entity.Student;
import com.example.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByLessonAndStudent(Lesson lesson, Student student);

    List<Attendance> findByStudent(Student student);

    List<Attendance> findByLesson(Lesson lesson);

    boolean existsByLessonAndStudent(Lesson lesson, Student student);

    @Query("""
            SELECT COUNT(a) FROM Attendance a 
            WHERE a.student.id = :studentId 
            AND a.lesson.teacherGroupSubject.subject.id = :subjectId 
            AND a.status = 'ABSENT'
            """)
    long countAbsencesByStudentAndSubject(@Param("studentId") Long studentId, @Param("subjectId") Long subjectId);
}



