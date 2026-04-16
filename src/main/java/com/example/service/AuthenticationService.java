package com.example.service;

import com.example.dto.mapper.EntityToDtoMapper;
import com.example.dto.request.LoginRequest;
import com.example.dto.request.RegisterRequest;
import com.example.dto.response.LoginResponse;
import com.example.dto.response.UserResponse;
import com.example.entity.User;
import com.example.enums.Role;
import com.example.exception.DuplicateUserException;
import com.example.repository.UserRepository;
import com.example.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EntityToDtoMapper mapper;

    public LoginResponse login(LoginRequest request) {
        log.info("Attempting login for email: {}", request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtTokenProvider.generateToken(authentication);
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse userResponse = mapper.toUserResponse(user);

        log.info("User logged in successfully: {}", request.getEmail());
        return LoginResponse.of(token, userResponse);
    }

    public LoginResponse register(RegisterRequest request) {
        log.info("Attempting registration for email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("User already exists with email: " + request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.valueOf(request.getRole().toUpperCase()))
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", request.getEmail());

        String token = jwtTokenProvider.generateTokenFromEmail(savedUser.getEmail());
        UserResponse userResponse = mapper.toUserResponse(savedUser);

        return LoginResponse.of(token, userResponse);
    }
}

