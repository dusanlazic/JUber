package com.nwt.juber.security;

import com.nwt.juber.config.AppProperties;
import com.nwt.juber.exception.InvalidAccessTokenException;
import com.nwt.juber.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

@Service
public class TokenProvider {

    private AppProperties appProperties;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createAccessToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(appProperties.getAuth().getTokenExpirationSeconds());

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(Date.from(now))
                .claim("type", TokenType.ACCESS)
                .setExpiration(Date.from(expiresAt))
                .signWith(getKey())
                .compact();
    }

    public String createEmailVerificationToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(appProperties.getAuth().getVerificationTokenExpirationMinutes() * 60);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(Date.from(now))
                .claim("type", TokenType.VERIFICATION)
                .setExpiration(Date.from(expiresAt))
                .signWith(getKey())
                .compact();
    }

    public UUID getUserIdFromToken(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(getKey()).build();
        Claims claims = parser.parseClaimsJws(token).getBody();

        return UUID.fromString(claims.getSubject());
    }

    public Claims readClaims(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(getKey()).build();
        return parser.parseClaimsJws(token).getBody();
    }

    public Boolean validateToken(String token, TokenType tokenType) {
        Claims claims = readClaims(token);
        if (claims.get("type") == null || !claims.get("type").equals(tokenType.name()))
            throw new InvalidAccessTokenException("Invalid token type.");

        return true;
    }

    private Key getKey() {
        byte[] keyBytes = appProperties.getAuth().getTokenSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
