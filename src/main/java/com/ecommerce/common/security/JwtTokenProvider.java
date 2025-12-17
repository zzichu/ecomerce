package com.ecommerce.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    // JWT 토큰 생성
    public String createToken(Authentication authentication) {
        String username = authentication.getName();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
    // JWT 토큰에서 사용자명 추출
    public String getUsernameFromToken(String token) {
        JwtParser parser = Jwts.parser()  
                .setSigningKey(secretKey) 
                .build();

        Jws<Claims> jws = parser.parseSignedClaims(token); //서명된 JWT
        Claims claims = jws.getPayload();

        return claims.getSubject();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build();

            parser.parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
