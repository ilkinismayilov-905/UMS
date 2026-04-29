package com.example.repository;

import com.example.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByUserId(Long userId);

    @Query("SELECT t FROM Teacher t JOIN FETCH t.user WHERE t.user.id = :userId")
    Optional<Teacher> findByUserIdWithDetails(@Param("userId") Long userId);
}
