package com.example.UsuarioService.service;

import com.example.UsuarioService.Model.Usuario;
import com.example.UsuarioService.dto.AuthDTO;
import com.example.UsuarioService.exception.RecursoNoEncontradoException;
import com.example.UsuarioService.repository.UsuarioRepository;
import com.example.UsuarioService.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock UsuarioRepository repository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtUtil jwtUtil;
    UsuarioService service;
    @BeforeEach void setUp() { service = new UsuarioService(repository, passwordEncoder, jwtUtil); }

    @Test void registrarUsuarioCodificaPassword() {
        Usuario u = new Usuario(); u.setNombre("Gabriel"); u.setEmail("gabriel@duoc.cl"); u.setPassword("password123");
        when(repository.existsByEmail(u.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hash");
        when(repository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));
        Usuario result = service.registrar(u);
        assertEquals("hash", result.getPassword());
    }

    @Test void loginValidoGeneraToken() {
        Usuario u = new Usuario(); u.setEmail("gabriel@duoc.cl"); u.setPassword("hash"); u.setNombre("Gabriel"); u.setRol("USER");
        AuthDTO.LoginRequest req = new AuthDTO.LoginRequest(); req.setEmail(u.getEmail()); req.setPassword("password123");
        when(repository.findByEmail(u.getEmail())).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("password123", "hash")).thenReturn(true);
        when(jwtUtil.generateToken(u.getEmail(), u.getRol())).thenReturn("token-jwt");
        assertEquals("token-jwt", service.login(req).getToken());
    }

    @Test void loginConPasswordIncorrectaFalla() {
        Usuario u = new Usuario(); u.setEmail("gabriel@duoc.cl"); u.setPassword("hash");
        AuthDTO.LoginRequest req = new AuthDTO.LoginRequest(); req.setEmail(u.getEmail()); req.setPassword("mala");
        when(repository.findByEmail(u.getEmail())).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("mala", "hash")).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> service.login(req));
    }

    @Test void buscarUsuarioInexistente() {
        when(repository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.findById(99));
    }
}
