package com.example.controller;

import com.example.dto.request.CreateSpecialtyRequest;
import com.example.dto.request.UpdateSpecialtyRequest;
import com.example.dto.response.SpecialtyResponse;
import com.example.service.SpecialtyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/specialties")
@RequiredArgsConstructor
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @GetMapping
    public ResponseEntity<List<SpecialtyResponse>> getAllSpecialties() {
        return ResponseEntity.ok(specialtyService.getAllSpecialties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyResponse> getSpecialtyById(@PathVariable Long id) {
        return ResponseEntity.ok(specialtyService.getSpecialtyById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SpecialtyResponse> getSpecialtyByName(@PathVariable String name) {
        return ResponseEntity.ok(specialtyService.getSpecialtyByName(name));
    }

    @PostMapping
    public ResponseEntity<SpecialtyResponse> createSpecialty(@Valid @RequestBody CreateSpecialtyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(specialtyService.createSpecialty(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecialtyResponse> updateSpecialty(@PathVariable Long id, @Valid @RequestBody UpdateSpecialtyRequest request) {
        return ResponseEntity.ok(specialtyService.updateSpecialty(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
        specialtyService.deleteSpecialty(id);
        return ResponseEntity.noContent().build();
    }
}

