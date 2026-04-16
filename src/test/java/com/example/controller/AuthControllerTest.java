package com.example.controller;

import com.example.dto.request.LoginRequest;
import com.example.dto.request.RegisterRequest;
import com.example.dto.response.LoginResponse;
import com.example.dto.response.UserResponse;
import com.example.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder()
                .email("user@school.com")
                .password("Password123!")
                .build();

        registerRequest = RegisterRequest.builder()
                .email("newuser@school.com")
                .password("SecurePassword123!")
                .firstName("John")
                .lastName("Doe")
                .role("TEACHER")
                .build();

        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .email("user@school.com")
                .firstName("John")
                .lastName("Doe")
                .role("TEACHER")
                .isActive(true)
                .build();

        loginResponse = LoginResponse.builder()
                .accessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                .tokenType("Bearer")
                .user(userResponse)
                .build();
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        // Arrange
        when(authenticationService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."))
                .andExpect(jsonPath("$.token_type").value("Bearer"))
                .andExpect(jsonPath("$.user.email").value("user@school.com"));
    }

    @Test
    void shouldRegisterSuccessfully() throws Exception {
        // Arrange
        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(loginResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.user.email").exists());
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsBlank() throws Exception {
        // Arrange
        LoginRequest invalidRequest = LoginRequest.builder()
                .email("")
                .password("Password123!")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsBlank() throws Exception {
        // Arrange
        LoginRequest invalidRequest = LoginRequest.builder()
                .email("user@school.com")
                .password("")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        // Arrange
        LoginRequest invalidRequest = LoginRequest.builder()
                .email("invalid-email")
                .password("Password123!")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenRegisteringWithMissingFirstName() throws Exception {
        // Arrange
        RegisterRequest invalidRequest = RegisterRequest.builder()
                .email("newuser@school.com")
                .password("SecurePassword123!")
                .firstName("")
                .lastName("Doe")
                .role("TEACHER")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenRegisteringWithMissingRole() throws Exception {
        // Arrange
        RegisterRequest invalidRequest = RegisterRequest.builder()
                .email("newuser@school.com")
                .password("SecurePassword123!")
                .firstName("John")
                .lastName("Doe")
                .role("")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}



