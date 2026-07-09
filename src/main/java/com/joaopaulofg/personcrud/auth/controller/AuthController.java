package com.joaopaulofg.personcrud.auth.controller;

import com.joaopaulofg.personcrud.auth.dto.AuthRequest;
import com.joaopaulofg.personcrud.auth.dto.AuthResponse;
import com.joaopaulofg.personcrud.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/validate")
    public AuthResponse validate(@RequestBody @Valid AuthRequest request) {
        return authService.validateApiKey(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Authorization") String authorizationHeader) {
        authService.logout(authorizationHeader);
    }
}