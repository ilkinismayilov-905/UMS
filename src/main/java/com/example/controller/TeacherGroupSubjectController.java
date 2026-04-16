package com.example.controller;

import com.example.dto.request.CreateTeacherGroupSubjectRequest;
import com.example.dto.response.TeacherGroupSubjectResponse;
import com.example.service.TeacherGroupSubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teacher-group-subjects")
@RequiredArgsConstructor
public class TeacherGroupSubjectController {

    private final TeacherGroupSubjectService tgsService;

    @GetMapping
    public ResponseEntity<List<TeacherGroupSubjectResponse>> getAllTeacherGroupSubjects() {
        return ResponseEntity.ok(tgsService.getAllTeacherGroupSubjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherGroupSubjectResponse> getTeacherGroupSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(tgsService.getTeacherGroupSubjectById(id));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TeacherGroupSubjectResponse>> getTeacherGroupSubjectsByTeacherId(@PathVariable Long teacherId) {
        return ResponseEntity.ok(tgsService.getTeacherGroupSubjectsByTeacherId(teacherId));
    }

    @PostMapping
    public ResponseEntity<TeacherGroupSubjectResponse> createTeacherGroupSubject(@Valid @RequestBody CreateTeacherGroupSubjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tgsService.createTeacherGroupSubject(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacherGroupSubject(@PathVariable Long id) {
        tgsService.deleteTeacherGroupSubject(id);
        return ResponseEntity.noContent().build();
    }
}

