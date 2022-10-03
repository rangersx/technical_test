package com.trello.clone.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.trello.clone.domain.Identity;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${com.trello.clone.jwt.access.exp.minutes:30}")
    private String accessExpiredTime;

    @Value("${com.trello.clone.jwt.refresh.exp.days:7}")
    private String refreshExpiredTime;

    private final String JWT_SECRET = "YdV9RQPnaDQuj9-dkfHIX-6yNSa89sjV7bLknx8AkqLiskEOlNps6ufxHY510CRoL9NKcSJD4da2EwX2m5Ypbw";
    private final String ISSUER_NAME = "Agile Identity Service";

    public DecodedJWT getAllClaimsFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(JWT_SECRET))
            .withIssuer(ISSUER_NAME)
            .build().verify(token);
    }

    public String getSessionIdFromToken(String token) {
        return getAllClaimsFromToken(token).getId();
    }

    public String getSubjectFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        return getAllClaimsFromToken(token).getClaims().get("roles") == null ? new ArrayList<>()
            : getAllClaimsFromToken(token).getClaims().get("roles").asList(String.class);
    }

    public LocalDateTime getExpirationDateFromToken(String token) {
        return LocalDateTime.ofInstant(
            getAllClaimsFromToken(token).getExpiresAt().toInstant(),
            ZoneId.systemDefault());
    }

    public String generateAccessToken(Identity identity) {
        return JWT.create()
            .withSubject(String.valueOf(identity.getId().id()))
            .withJWTId(String.valueOf(identity.getSessionId()))
            .withIssuedAt(new Date())
            .withIssuer(ISSUER_NAME)
            .withExpiresAt(buildExpirationDate(accessExpiredTime))
            .sign(Algorithm.HMAC512(JWT_SECRET));
    }

    public String generateRefreshToken(Identity identity) {
        return JWT.create()
            .withSubject(String.valueOf(identity.getId().id()))
            .withJWTId(String.valueOf(identity.getSessionId()))
            .withIssuedAt(new Date())
            .withIssuer(ISSUER_NAME)
            .withExpiresAt(buildExpirationDate(refreshExpiredTime))
            .sign(Algorithm.HMAC512(JWT_SECRET));
    }

    private @NotNull Date buildExpirationDate(String expiredTime) {
        return StringUtils.isBlank(expiredTime) || StringUtils.equals(expiredTime, "0") ?
            Date.from(new Date().toInstant().plus(999, ChronoUnit.CENTURIES)) :
            Date.from(new Date().toInstant().plus(Long.parseLong(expiredTime), ChronoUnit.MINUTES));
    }

}
