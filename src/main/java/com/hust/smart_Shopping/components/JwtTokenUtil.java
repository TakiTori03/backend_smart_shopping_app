package com.hust.smart_Shopping.components;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.hust.smart_Shopping.exceptions.payload.DataNotFoundException;
import com.hust.smart_Shopping.models.Token;
import com.hust.smart_Shopping.repositories.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final TokenRepository tokenRepository;

    @Value("${jwt.expiration}")
    private Integer expiration; // lưu trong biến môi trường

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(new HashMap<>(), userDetails);
    }

    // generate token using jwt utility class and return token as string
    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {
        try {
            return Jwts
                    .builder()
                    .setClaims(extractClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 100))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    // generate token using jwt utility class and return token as string
    public String generateRefreshToken(Map<String, Object> extractClaims, UserDetails userDetails) {
        try {
            return Jwts
                    .builder()
                    .setClaims(extractClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expirationRefreshToken * 100))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    // decode and get the key
    private Key getSignInKey() {
        // decode SECRET_KEY
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // if token is valid by checking if token is expired for current user
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        Token existingToken = tokenRepository.findByToken(token).orElseThrow(() -> new DataNotFoundException(""));

        if (existingToken == null
                || existingToken.isRevoked()
                || !existingToken.getUser().isActivated()) {
            return false;
        }
        return email.equals(userDetails.getUsername()) && !isTokenExpirated(token);
    }

    // extract user from token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // if token expirated
    public boolean isTokenExpirated(String token) {
        return extractExpiration(token).before(new Date());
    }

    // get expiration data from token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
