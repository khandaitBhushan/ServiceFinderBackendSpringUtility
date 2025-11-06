package com.org.ServiceFinder.controller;

import com.org.ServiceFinder.dto.AuthRequest;
import com.org.ServiceFinder.dto.AuthResponse;
import com.org.ServiceFinder.model.User;
import com.org.ServiceFinder.model.Role;
import com.org.ServiceFinder.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        AuthResponse response = authService.register(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}