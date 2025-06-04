package com.evega.website_backend.security;
import com.evega.website_backend.models.UserToken;
import com.evega.website_backend.repositories.UserTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationTime;
    private final UserTokenRepository userTokenRepository;

    public JwtService(
            @Value("${JWT_SECRET}") String secret,
            @Value("${JWT_EXPIRATION}") long expiration,
            UserTokenRepository userTokenRepository
    ) {
        this.secretKey = generateKey(secret);
        this.expirationTime = expiration;
        this.userTokenRepository = userTokenRepository;
    }

    private SecretKey generateKey(String secret) {
        if (secret == null || secret.trim().isEmpty()) {
            secret = UUID.randomUUID().toString();
        }
        return new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public String getSubject() {
    	UUID randomUUID = UUID.randomUUID();
    	String subject = randomUUID.toString();
        return subject;
    }

    public String createToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        String subject = this.getSubject();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username, String deviceId) {
        final String extractedUsername = extractUsername(token);
        if (!extractedUsername.equals(username) || isTokenExpired(token)) {
            return false;
        }

        Optional<UserToken> userTokenOptional = userTokenRepository.findByToken(token);
        return userTokenOptional.isPresent() &&
                userTokenOptional.get().getDeviceId().equals(deviceId) &&
                userTokenOptional.get().getInvalidatedAt() == null;
    }
    public void invalidateToken(String token) {
        Optional<UserToken> userTokenOptional = userTokenRepository.findByToken(token);
        userTokenOptional.ifPresent(userToken -> {
            if (userToken.getInvalidatedAt() == null) { // Check if NOT already invalidated
                userToken.setInvalidatedAt(LocalDateTime.now());
                userTokenRepository.save(userToken);
            }
            // If invalidatedAt is not null, we do nothing.
        });
        // If the token is not found, it's already considered invalid or doesn't exist.
        // You might want to log this for debugging purposes.
    }
}