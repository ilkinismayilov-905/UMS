package com.example.controller;

import com.example.dto.request.CreateGroupRequest;
import com.example.dto.request.UpdateGroupRequest;
import com.example.dto.response.GroupResponse;
import com.example.dto.response.SpecialtyResponse;
import com.example.service.GroupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController groupController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private GroupResponse groupResponse;
    private CreateGroupRequest createGroupRequest;
    private UpdateGroupRequest updateGroupRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();

        groupResponse = new GroupResponse(1L, "651.21", new SpecialtyResponse(1L, "Computer Science"));
        createGroupRequest = new CreateGroupRequest("651.21", 1L);
        updateGroupRequest = new UpdateGroupRequest( 1L);
    }

    @Test
    void shouldGetAllGroupsSuccessfully() throws Exception {
        // Arrange
        List<GroupResponse> groups = List.of(groupResponse);
        when(groupService.getAllGroups()).thenReturn(groups);

        // Act & Assert
        mockMvc.perform(get("/api/v1/groups")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].groupNumber").value("651.21"));
    }

    @Test
    void shouldGetGroupByIdSuccessfully() throws Exception {
        // Arrange
        when(groupService.getGroupById(1L)).thenReturn(groupResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/groups/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.groupNumber").value("651.21"));
    }

    @Test
    void shouldGetGroupByGroupNumberSuccessfully() throws Exception {
        // Arrange
        when(groupService.getGroupByGroupNumber("651.21")).thenReturn(groupResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/groups/number/651.21")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupNumber").value("651.21"));
    }

    @Test
    void shouldGetGroupsBySpecialtyIdSuccessfully() throws Exception {
        // Arrange
        List<GroupResponse> groups = List.of(groupResponse);
        when(groupService.getGroupsBySpecialtyId(1L)).thenReturn(groups);

        // Act & Assert
        mockMvc.perform(get("/api/v1/groups/specialty/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].specialty.id").value(1L));
    }

    @Test
    void shouldCreateGroupSuccessfully() throws Exception {
        // Arrange
        when(groupService.createGroup(any(CreateGroupRequest.class))).thenReturn(groupResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createGroupRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.groupNumber").value("651.21"));
    }

    @Test
    void shouldUpdateGroupSuccessfully() throws Exception {
        // Arrange
        GroupResponse updatedResponse = new GroupResponse(1L, "651.21-Update", new SpecialtyResponse(1L, "Computer Science"));
        when(groupService.updateGroup(eq(1L), any(UpdateGroupRequest.class))).thenReturn(updatedResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/groups/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateGroupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupNumber").value("651.21-Update"));
    }

    @Test
    void shouldDeleteGroupSuccessfully() throws Exception {
        // Arrange
        doNothing().when(groupService).deleteGroup(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/groups/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}