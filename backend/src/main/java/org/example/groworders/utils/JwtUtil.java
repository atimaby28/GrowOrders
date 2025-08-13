package org.example.groworders.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final String SECRET = "abcdeffghijklmnopqrstuvwxyz0123456";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final Long EXP = 1000 * 60 * 120L;

    public static String generateToken(String email, Long id) {

        Map<String, String> claims =  new HashMap<>();
        claims.put("id", "" + id);
        claims.put("email", email);
        claims.put("role", "USER");

        return Jwts.builder()
                .setSubject(email)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXP))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }


    public static String getValue(Claims claims, String key) {

        return (String) claims.get(key);
    }

    public static Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
