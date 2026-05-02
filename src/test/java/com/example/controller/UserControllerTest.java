package com.example.controller;

import com.example.dto.request.CreateUserRequest;
import com.example.dto.request.UpdateUserRequest;
import com.example.dto.response.UserResponse;
import com.example.service.UserService;
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
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UserResponse userResponse;
    private CreateUserRequest createUserRequest;
    private UpdateUserRequest updateUserRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        userResponse = new UserResponse(1L, "user@school.com", "John", "Doe", "TEACHER", true);
        createUserRequest = new CreateUserRequest("user@school.com", "SecurePass123!", "John", "Doe", "TEACHER");
        updateUserRequest = new UpdateUserRequest("John-Updated", "Doe-Updated", true);
    }

    @Test
    void shouldGetAllUsersSuccessfully() throws Exception {
        // Arrange
        List<UserResponse> users = List.of(userResponse);
        when(userService.getAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].email").value("user@school.com"));
    }

    @Test
    void shouldGetUserByIdSuccessfully() throws Exception {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("user@school.com"));
    }

    @Test
    void shouldGetUserByEmailSuccessfully() throws Exception {
        // Arrange
        when(userService.getUserByEmail("user@school.com")).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/email/user@school.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@school.com"));
    }

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        // Arrange
        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("user@school.com"))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {
        // Arrange
        UserResponse updated = new UserResponse(1L, "user@school.com", "John-Updated", "Doe-Updated", "TEACHER", true);
        when(userService.updateUser(eq(1L), any(UpdateUserRequest.class))).thenReturn(updated);

        // Act & Assert
        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John-Updated"));
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        // Arrange
        doNothing().when(userService).deleteUser(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingUserWithBlankEmail() throws Exception {
        // Arrange
        CreateUserRequest invalid = new CreateUserRequest("", "Pass123!", "John", "Doe", "TEACHER");

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingUserWithInvalidEmail() throws Exception {
        // Arrange
        CreateUserRequest invalid = new CreateUserRequest("invalid-email", "Pass123!", "John", "Doe", "TEACHER");

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingUserWithBlankFirstName() throws Exception {
        // Arrange
        CreateUserRequest invalid = new CreateUserRequest("user@school.com", "Pass123!", "", "Doe", "TEACHER");

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingUserWithBlankFirstName() throws Exception {
        // Arrange
        UpdateUserRequest invalid = new UpdateUserRequest("", "Doe", true);

        // Act & Assert
        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}
