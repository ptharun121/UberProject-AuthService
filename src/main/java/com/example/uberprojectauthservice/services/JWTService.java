package com.example.uberprojectauthservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.expiry}")
    private int expiry;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * This method will create JWT token for the payloads.
     * @return
     */
    public String createToken(Map<String, Object> payLoad, String email) {
        Date date = new Date();
        Date expiryDate = new Date(date.getTime() + expiry*1000L);

        return Jwts.builder()
                .claims(payLoad)
                .issuedAt(date)
                .expiration(expiryDate)
                .subject(email)
                .signWith(this.getSigninKey())
                .compact();

    }

    public String createToken(String email) {
        return createToken(new HashMap<>(), email);
    }

    public Key getSigninKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Claims extractPayloads(String token) {
        return Jwts.parser()
                .setSigningKey(this.getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractPayloads(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public String extractEmail(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * This method checks if the token is expired or not.
     * @param token
     * @return
     */
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * This method will check if teh token is valid based on the email address.
     * @param token
     * @param email
     * @
     */
    public Boolean validateToken(String token, String email) {
        final String userEmailFetchedFromToken = extractEmail(token);
        return userEmailFetchedFromToken.equals(email) && !isTokenExpired(token);
    }

    public String extractPhoneNumber(String token) {
        Claims claim = extractPayloads(token);
        Object phoneNumber = claim.get("phone");
        return phoneNumber.toString();
    }
}
