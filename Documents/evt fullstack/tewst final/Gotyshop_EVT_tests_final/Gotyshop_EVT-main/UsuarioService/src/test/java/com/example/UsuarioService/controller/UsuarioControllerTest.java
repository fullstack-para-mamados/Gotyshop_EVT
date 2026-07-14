package com.example.UsuarioService.controller;

import com.example.UsuarioService.Model.Usuario;
import com.example.UsuarioService.dto.AuthDTO;
import com.example.UsuarioService.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    private UsuarioController controller;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new UsuarioController(usuarioService);
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Administrador");
        usuario.setEmail("admin@gotystore.com");
        usuario.setPassword("password123");
        usuario.setRol("ADMIN");
    }

    @Test
    void loginRetornaRespuestaCorrecta() {
        AuthDTO.LoginRequest request = new AuthDTO.LoginRequest();
        request.setEmail("admin@gotystore.com");
        request.setPassword("password123");
        AuthDTO.LoginResponse expected = new AuthDTO.LoginResponse(
                "token", "admin@gotystore.com", "Administrador", "ADMIN");

        when(usuarioService.login(request)).thenReturn(expected);

        ResponseEntity<AuthDTO.LoginResponse> response = controller.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(expected, response.getBody());
        verify(usuarioService).login(request);
    }

    @Test
    void registroRetornaCreated() {
        when(usuarioService.registrar(usuario)).thenReturn(usuario);

        ResponseEntity<Usuario> response = controller.registro(usuario);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(usuario, response.getBody());
        verify(usuarioService).registrar(usuario);
    }

    @Test
    void getAllRetornaUsuarios() {
        when(usuarioService.findAll()).thenReturn(List.of(usuario));

        ResponseEntity<List<Usuario>> response = controller.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(usuarioService).findAll();
    }

    @Test
    void getByIdRetornaUsuario() {
        when(usuarioService.findById(1)).thenReturn(usuario);

        ResponseEntity<Usuario> response = controller.getById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(usuario, response.getBody());
        verify(usuarioService).findById(1);
    }

    @Test
    void existeRetornaResultado() {
        when(usuarioService.existsByEmail("admin@gotystore.com")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.existe("admin@gotystore.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(usuarioService).existsByEmail("admin@gotystore.com");
    }

    @Test
    void buscarPorEmailRetornaUsuario() {
        when(usuarioService.findByEmail("admin@gotystore.com")).thenReturn(usuario);

        ResponseEntity<Usuario> response = controller.buscarPorEmail("admin@gotystore.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(usuario, response.getBody());
        verify(usuarioService).findByEmail("admin@gotystore.com");
    }

    @Test
    void deleteRetornaNoContent() {
        ResponseEntity<Void> response = controller.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(usuarioService).deleteById(1);
    }
}
