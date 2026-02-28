package com.dee.district_employment_exchange.controller;

import com.dee.district_employment_exchange.dto.ApiResponse;
import com.dee.district_employment_exchange.dto.LoginRequest;
import com.dee.district_employment_exchange.dto.LoginResponse;
import com.dee.district_employment_exchange.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.success("Login successful", response));
    }
}