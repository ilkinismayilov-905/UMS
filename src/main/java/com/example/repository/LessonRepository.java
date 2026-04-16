package com.example.repository;

import com.example.entity.Lesson;
import com.example.entity.TeacherGroupSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByTeacherGroupSubjectAndIsActiveTrue(TeacherGroupSubject teacherGroupSubject);

    Optional<Lesson> findByIdAndIsActiveTrue(Long id);

    List<Lesson> findByTeacherGroupSubjectAndStartTimeBetweenAndIsActiveTrue(
            TeacherGroupSubject teacherGroupSubject,
            LocalDateTime startTime,
            LocalDateTime endTime
    );
}

