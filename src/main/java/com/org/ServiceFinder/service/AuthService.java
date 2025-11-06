package com.org.ServiceFinder.service;

import com.org.ServiceFinder.dto.AuthRequest;
import com.org.ServiceFinder.dto.AuthResponse;
import com.org.ServiceFinder.model.User;
import com.org.ServiceFinder.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(AuthRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getEmail(), user.getRole().name(), "Login successful!", true);
    }

    public AuthResponse register(User user) {
        User savedUser = userService.registerUser(user);
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().name());
        return new AuthResponse(token, savedUser.getEmail(), savedUser.getRole().name(), "Registration successful!", true);
    }
}