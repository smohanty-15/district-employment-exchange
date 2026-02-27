package com.dee.district_employment_exchange.service;

import com.dee.district_employment_exchange.dto.LoginRequest;
import com.dee.district_employment_exchange.dto.LoginResponse;
import com.dee.district_employment_exchange.entity.User;
import com.dee.district_employment_exchange.repository.UserRepository;
import com.dee.district_employment_exchange.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public LoginResponse login(LoginRequest request) {

        // Step 1 - Verify email + password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Step 2 - Generate token
        String token = jwtUtils.generateToken(request.getEmail());

        // Step 3 - Get user from DB
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 4 - Return token + user info
        return new LoginResponse(
                token,
                "Bearer",
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}