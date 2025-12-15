package com.gkfcsolution.gkfcsolutionhotel.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created on 2025 at 22:32
 * File: null.java
 * Project: Spring-Boot-Spring-Security-JWT-Complete
 *
 * @author Frank GUEKENG
 * @date 21/09/2025
 * @time 22:32
 */

@Component
public class JwtUtil {

    private final Key signingKey;
    private final long jwtExpirationMs;

    public JwtUtil(@Value("${jwt.secret}") String base64Secret,
                   @Value("${jwt.expiration-ms:36000000}") long jwtExpirationMs) {
        // Le secret doit être encodé en Base64 et suffisamment long (>= 256 bits pour HS256)
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtExpirationMs = jwtExpirationMs;
    }

    // --- Extraire des claims ---
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // --- Création de token ---
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Ajouter les rôles dans les claims si utile
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // --- Validation ---
    private boolean isTokenExpired(String token) {
        Date exp = extractExpiration(token);
        return exp.before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // utilitaire si tu veux récupérer les roles depuis le token
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object o = claims.get("roles");
        if (o instanceof List) {
            return ((List<?>) o).stream().map(Object::toString).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}