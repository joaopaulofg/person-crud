package com.joaopaulofg.personcrud.auth.service;

import com.joaopaulofg.personcrud.auth.dto.AuthRequest;
import com.joaopaulofg.personcrud.auth.dto.AuthResponse;
import com.joaopaulofg.personcrud.config.AppSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String SUBJECT = "api-client";

    private final AppSecurityProperties properties;
    private final JwtService jwtService;
    private final RedisTokenService redisTokenService;

    public AuthResponse validateApiKey(AuthRequest request) {
        if (!properties.apiKey().equals(request.apiKey())) {
            throw new BadCredentialsException("API Key inválida");
        }

        String token = jwtService.generateToken(SUBJECT);
        String tokenId = jwtService.getTokenId(token);
        long expiresInSeconds = jwtService.getExpiresInSeconds();

        redisTokenService.save(tokenId, SUBJECT, expiresInSeconds);

        return new AuthResponse(
                token,
                "Bearer",
                expiresInSeconds
        );
    }

    public void logout(String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        String tokenId = jwtService.getTokenId(token);

        redisTokenService.delete(tokenId);
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("Token não informado");
        }

        return authorizationHeader.substring(7);
    }
}