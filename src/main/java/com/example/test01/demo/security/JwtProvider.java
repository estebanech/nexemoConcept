package com.example.test01.demo.security;

import com.example.test01.demo.entity.UserIn;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    @Value("${app.jwtRefreshSecret}")
    private String jwtRefreshSecret;

    @Value("${app.jwtRefreshExpirationInMs}")
    private int jwtRefreshExpirationInMs;

    private SecretKey decodeKeyFromString(final String keyStr) {
        /* Decodes a Base64 encoded String into a byte array */
        final byte[] decodedKey = Base64.getDecoder().decode(keyStr);

        /* Constructs a secret key from the given byte array */
        final SecretKey secretKey = new SecretKeySpec(decodedKey, 0,
                decodedKey.length, "HmacSHA512");

        return secretKey;
    }

    public String generateToken(final UserIn user){
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime expire = now.plusSeconds(jwtExpirationInMs);
        return Jwts.builder()
                .setSubject(Long.toString(user.getId()))
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expire.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(decodeKeyFromString(jwtSecret),SignatureAlgorithm.HS512)
                .compact();
    }

    public Long getUserIdFromJWT(final String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public String generateRefresh(final UserIn user){
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime expire = now.plusSeconds(jwtRefreshExpirationInMs);
        return Jwts.builder()
                .setSubject(Long.toString(user.getId()))
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expire.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(decodeKeyFromString(jwtRefreshSecret),SignatureAlgorithm.HS512)
                .compact();
    }

    public Long getUserIdFromRefreshJWT(final String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtRefreshSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean ValidateRefreshToken(final String authToken){
        return validate(authToken,jwtRefreshSecret);
    }

    public boolean validateToken(final String authToken) {
        return validate(authToken,jwtSecret);
    }

    private boolean validate(final String Token, final String signature){
        try {
            Jwts.parser().setSigningKey(signature).parseClaimsJws(Token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }

}
