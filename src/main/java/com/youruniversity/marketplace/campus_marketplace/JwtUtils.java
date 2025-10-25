package com.youruniversity.marketplace.campus_marketplace;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);
    private static final SecretKey JWT_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long JWT_EXPIRATION_MS = 86400000; // 1 day
    public String generateToken(String email) {
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION_MS))
            .signWith(JWT_SECRET, SignatureAlgorithm.HS512)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("JWT validation error: {}", e.getMessage());
            return false;
        }
    }
    
    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Error getting email from token: {}", e.getMessage());
            return null;
        }
    }
    }

