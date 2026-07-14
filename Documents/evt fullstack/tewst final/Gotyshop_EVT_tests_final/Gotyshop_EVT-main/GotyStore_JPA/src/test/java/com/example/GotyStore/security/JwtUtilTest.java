package com.example.GotyStore.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private static final String SECRET =
            "GotyStoreSecretKey2024GotyStoreSecretKey2024GotyStoreSecretKey2024";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET);
    }

    @Test
    void extraeEmailYValidaToken() {
        String token = token("admin@gotystore.com", SECRET);

        assertTrue(jwtUtil.validateToken(token));
        assertEquals("admin@gotystore.com", jwtUtil.extractEmail(token));
    }

    @Test
    void tokenAlteradoEsInvalido() {
        String token = token("admin@gotystore.com", SECRET);

        assertFalse(jwtUtil.validateToken(token + "x"));
    }

    @Test
    void tokenConOtraFirmaEsInvalido() {
        String token = token("admin@gotystore.com",
                "ClaveDiferenteParaPruebasClaveDiferenteParaPruebasClaveDiferente");

        assertFalse(jwtUtil.validateToken(token));
    }

    private String token(String email, String secret) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder()
                .setSubject(email)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
