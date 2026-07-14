package com.example.UsuarioService.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret",
                "GotyStoreSecretKey2024GotyStoreSecretKey2024GotyStoreSecretKey2024");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 60000L);
    }

    @Test
    void generaYValidaToken() {
        String token = jwtUtil.generateToken("admin@gotystore.com", "ADMIN");

        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals("admin@gotystore.com", jwtUtil.extractEmail(token));
    }

    @Test
    void tokenAlteradoEsInvalido() {
        String token = jwtUtil.generateToken("admin@gotystore.com", "ADMIN");

        assertFalse(jwtUtil.validateToken(token + "x"));
    }

    @Test
    void tokenFirmadoConOtraClaveEsInvalido() {
        JwtUtil otro = new JwtUtil();
        ReflectionTestUtils.setField(otro, "secret",
                "ClaveDiferenteParaPruebasClaveDiferenteParaPruebasClaveDiferente");
        ReflectionTestUtils.setField(otro, "expiration", 60000L);
        String token = otro.generateToken("admin@gotystore.com", "ADMIN");

        assertFalse(jwtUtil.validateToken(token));
    }
}
