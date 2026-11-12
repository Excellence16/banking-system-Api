package com.excellence.bankingsystemapi.security.jwt;

import com.excellence.bankingsystemapi.security.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long jwtExpiration;

    public JwtService(@Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.expiration}") long jwtExpiration) {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.jwtExpiration = jwtExpiration;
    }

    /**
     * Generates a JWT without additional claims.
     */
    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    /**
     * Generates a JWT with additional claims.
     */
    public String generateToken(Map<String, Object> extraClaims, User user) {

        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Extracts the JWT subject.
     */
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    /**
     * Extracts User ID from JWT subject.
     */
    public Long extractUserId(String token) {

        return Long.parseLong(extractSubject(token));
    }

    /**
     * Extracts the user's email.
     */
    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    /**
     * Extracts the expiration date.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Generic method for extracting any claim.
     */
    public <T> T extractClaim(String token,
            Function<Claims, T> claimsResolver) {

        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    /**
     * Checks whether the token has expired.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates the JWT.
     */
    public boolean isTokenValid(String token, User user) {

        String subject = extractSubject(token);

        return subject.equals(user
                .getId()
                .toString()) && !isTokenExpired(token);
    }

    /**
     * Extracts all claims from the JWT.
     */
    private Claims extractAllClaims(String token) {

        return Jwts
                .parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}