package com.joaopaulofg.personcrud.auth.service;

import com.joaopaulofg.personcrud.config.AppSecurityProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String ISSUER = "person-crud";

    private final AppSecurityProperties properties;

    public String generateToken(String subject) {
        try {
            Instant now = Instant.now();
            Instant expiration = now.plusSeconds(properties.jwtExpirationMinutes() * 60);

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(subject)
                    .issuer(ISSUER)
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(expiration))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("scope", "api")
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet
            );

            signedJWT.sign(new MACSigner(properties.jwtSecret().getBytes()));

            return signedJWT.serialize();
        } catch (JOSEException exception) {
            throw new IllegalStateException("Erro ao gerar JWT", exception);
        }
    }

    public boolean isValid(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWSVerifier verifier = new MACVerifier(properties.jwtSecret().getBytes());

            boolean signatureValid = signedJWT.verify(verifier);
            boolean issuerValid = ISSUER.equals(signedJWT.getJWTClaimsSet().getIssuer());
            boolean notExpired = signedJWT.getJWTClaimsSet()
                    .getExpirationTime()
                    .after(new Date());

            return signatureValid && issuerValid && notExpired;
        } catch (ParseException | JOSEException exception) {
            return false;
        }
    }

    public String getSubject(String token) {
        try {
            return SignedJWT.parse(token)
                    .getJWTClaimsSet()
                    .getSubject();
        } catch (ParseException exception) {
            throw new IllegalArgumentException("Token inválido", exception);
        }
    }

    public String getTokenId(String token) {
        try {
            return SignedJWT.parse(token)
                    .getJWTClaimsSet()
                    .getJWTID();
        } catch (ParseException exception) {
            throw new IllegalArgumentException("Token inválido", exception);
        }
    }

    public long getExpiresInSeconds() {
        return properties.jwtExpirationMinutes() * 60;
    }
}