package com.example.service;

import com.example.dto.mapper.EntityToDtoMapper;
import com.example.dto.request.CreateGradeRequest;
import com.example.dto.request.UpdateGradeRequest;
import com.example.dto.response.GradeResponse;
import com.example.entity.Grade;
import com.example.entity.Student;
import com.example.entity.Subject;
import com.example.entity.Teacher;
import com.example.enums.GradeStatus;
import com.example.exception.GradeNotFoundException;
import com.example.exception.InvalidGradeValueException;
import com.example.exception.StudentNotFoundException;
import com.example.exception.SubjectNotFoundException;
import com.example.exception.TeacherNotFoundException;
import com.example.repository.GradeRepository;
import com.example.repository.StudentRepository;
import com.example.repository.SubjectRepository;
import com.example.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final EntityToDtoMapper mapper;

    @Transactional(readOnly = true)
    public GradeResponse getGradeById(Long id) {
        log.info("Fetching grade with id: {}", id);
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new GradeNotFoundException("Grade not found with id: " + id));
        return mapper.toGradeResponse(grade);
    }

    @Transactional(readOnly = true)
    public List<GradeResponse> getGradesByStudentId(Long studentId) {
        log.info("Fetching grades for student id: {}", studentId);
        return gradeRepository.findAllByStudentId(studentId)
                .stream()
                .map(mapper::toGradeResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GradeResponse> getGradesByTeacherId(Long teacherId) {
        log.info("Fetching grades for teacher id: {}", teacherId);
        return gradeRepository.findAllByTeacherId(teacherId)
                .stream()
                .map(mapper::toGradeResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GradeResponse> getAllGrades() {
        log.info("Fetching all grades");
        return gradeRepository.findAll()
                .stream()
                .map(mapper::toGradeResponse)
                .toList();
    }

    public GradeResponse createGrade(CreateGradeRequest request) {
        log.info("Creating new grade");

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + request.getStudentId()));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException("Subject not found with id: " + request.getSubjectId()));

        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id: " + request.getTeacherId()));

        validateGradeValues(request);

        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setTeacher(teacher);
        grade.setAttendanceScore(request.getAttendanceScore());
        grade.setSeminarScore(request.getSeminarScore());
        grade.setCol1(request.getCol1());
        grade.setCol2(request.getCol2());
        grade.setCol3(request.getCol3());
        grade.setExamScore(request.getExamScore());

        int totalScore = calculateTotalScore(request);
        grade.setTotalScore(totalScore);
        grade.setStatus(getGradeStatus(totalScore));

        Grade savedGrade = gradeRepository.save(grade);
        log.info("Grade created successfully with id: {}", savedGrade.getId());

        return mapper.toGradeResponse(savedGrade);
    }

    public GradeResponse updateGrade(Long id, UpdateGradeRequest request) {
        log.info("Updating grade with id: {}", id);

        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new GradeNotFoundException("Grade not found with id: " + id));

        validateGradeValues(request);

        if (request.getAttendanceScore() != null) {
            grade.setAttendanceScore(request.getAttendanceScore());
        }
        if (request.getSeminarScore() != null) {
            grade.setSeminarScore(request.getSeminarScore());
        }
        if (request.getCol1() != null) {
            grade.setCol1(request.getCol1());
        }
        if (request.getCol2() != null) {
            grade.setCol2(request.getCol2());
        }
        if (request.getCol3() != null) {
            grade.setCol3(request.getCol3());
        }
        if (request.getExamScore() != null) {
            grade.setExamScore(request.getExamScore());
        }

        int totalScore = calculateTotalScore(grade);
        grade.setTotalScore(totalScore);
        grade.setStatus(getGradeStatus(totalScore));

        Grade updatedGrade = gradeRepository.save(grade);
        log.info("Grade updated successfully with id: {}", updatedGrade.getId());

        return mapper.toGradeResponse(updatedGrade);
    }

    public void deleteGrade(Long id) {
        log.info("Deleting grade with id: {}", id);

        if (!gradeRepository.existsById(id)) {
            throw new GradeNotFoundException("Grade not found with id: " + id);
        }

        gradeRepository.deleteById(id);
        log.info("Grade deleted successfully with id: {}", id);
    }

    private void validateGradeValues(CreateGradeRequest request) {
        if ((request.getAttendanceScore() != null && (request.getAttendanceScore() < 0 || request.getAttendanceScore() > 10)) ||
                (request.getSeminarScore() != null && (request.getSeminarScore() < 0 || request.getSeminarScore() > 10)) ||
                (request.getCol1() != null && (request.getCol1() < 0 || request.getCol1() > 10)) ||
                (request.getCol2() != null && (request.getCol2() < 0 || request.getCol2() > 10)) ||
                (request.getCol3() != null && (request.getCol3() < 0 || request.getCol3() > 10)) ||
                (request.getExamScore() != null && (request.getExamScore() < 0 || request.getExamScore() > 50))) {
            throw new InvalidGradeValueException("Invalid grade value. Scores must be within allowed ranges.");
        }
    }

    private void validateGradeValues(UpdateGradeRequest request) {
        if ((request.getAttendanceScore() != null && (request.getAttendanceScore() < 0 || request.getAttendanceScore() > 10)) ||
                (request.getSeminarScore() != null && (request.getSeminarScore() < 0 || request.getSeminarScore() > 10)) ||
                (request.getCol1() != null && (request.getCol1() < 0 || request.getCol1() > 10)) ||
                (request.getCol2() != null && (request.getCol2() < 0 || request.getCol2() > 10)) ||
                (request.getCol3() != null && (request.getCol3() < 0 || request.getCol3() > 10)) ||
                (request.getExamScore() != null && (request.getExamScore() < 0 || request.getExamScore() > 50))) {
            throw new InvalidGradeValueException("Invalid grade value. Scores must be within allowed ranges.");
        }
    }

    private int calculateTotalScore(CreateGradeRequest request) {
        int total = 0;
        if (request.getAttendanceScore() != null) total += request.getAttendanceScore();
        if (request.getSeminarScore() != null) total += request.getSeminarScore();
        if (request.getCol1() != null) total += request.getCol1();
        if (request.getCol2() != null) total += request.getCol2();
        if (request.getCol3() != null) total += request.getCol3();
        if (request.getExamScore() != null) total += request.getExamScore();
        return total;
    }

    private int calculateTotalScore(Grade grade) {
        int total = 0;
        if (grade.getAttendanceScore() != null) total += grade.getAttendanceScore();
        if (grade.getSeminarScore() != null) total += grade.getSeminarScore();
        if (grade.getCol1() != null) total += grade.getCol1();
        if (grade.getCol2() != null) total += grade.getCol2();
        if (grade.getCol3() != null) total += grade.getCol3();
        if (grade.getExamScore() != null) total += grade.getExamScore();
        return total;
    }

    private GradeStatus getGradeStatus(int totalScore) {
        if (totalScore >= 80) return GradeStatus.PASSED;
        if (totalScore >= 60) return GradeStatus.PASSED;
        if (totalScore >= 40) return GradeStatus.FAILED_BY_TOTAL;
        return GradeStatus.FAILED_BY_EXAM;
    }
}
