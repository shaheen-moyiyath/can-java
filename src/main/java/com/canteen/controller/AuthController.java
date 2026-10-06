package com.canteen.controller;

import com.canteen.dto.ApiResponse;
import com.canteen.dto.AuthRequest;
import com.canteen.dto.AuthResponse;
import com.canteen.dto.RegisterRequest;
import com.canteen.entity.User;
import com.canteen.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and JWT authentication")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "User / Admin Login", description = "Authenticates credentials and returns a JWT Bearer token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    @PostMapping("/register")
    @Operation(summary = "Student / User Registration", description = "Registers a new student account and returns JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("User registered successfully", response));
    }

    @GetMapping("/me")
    @Operation(summary = "Get Current User Profile", description = "Returns details of the currently authenticated user")
    public ResponseEntity<ApiResponse<AuthResponse>> getCurrentUser() {
        User user = authService.getCurrentAuthenticatedUser();
        AuthResponse response = new AuthResponse(
                null,
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name()
        );
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
