package com.example.controller;

import com.example.dto.request.CreateGroupRequest;
import com.example.dto.request.UpdateGroupRequest;
import com.example.dto.response.GroupResponse;
import com.example.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @GetMapping("/number/{groupNumber}")
    public ResponseEntity<GroupResponse> getGroupByGroupNumber(@PathVariable String groupNumber) {
        return ResponseEntity.ok(groupService.getGroupByGroupNumber(groupNumber));
    }

    @GetMapping("/specialty/{specialtyId}")
    public ResponseEntity<List<GroupResponse>> getGroupsBySpecialtyId(@PathVariable Long specialtyId) {
        return ResponseEntity.ok(groupService.getGroupsBySpecialtyId(specialtyId));
    }

    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createGroup(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupResponse> updateGroup(@PathVariable Long id, @Valid @RequestBody UpdateGroupRequest request) {
        return ResponseEntity.ok(groupService.updateGroup(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}

