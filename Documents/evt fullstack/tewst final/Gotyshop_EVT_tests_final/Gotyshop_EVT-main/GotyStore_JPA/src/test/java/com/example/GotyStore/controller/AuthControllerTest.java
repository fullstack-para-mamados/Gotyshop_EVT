package com.example.GotyStore.controller;

import com.example.GotyStore.service.UsuarioFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UsuarioFeignClient usuarioFeignClient;

    private AuthController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new AuthController(usuarioFeignClient);
    }

    @Test
    void verificarUsuarioExistenteRetorna200() {
        when(usuarioFeignClient.existeUsuario("admin@gotystore.com")).thenReturn(true);

        ResponseEntity<Map<String, Object>> response =
                controller.verificar("admin@gotystore.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().get("existe"));
        assertEquals("Usuario encontrado en el sistema", response.getBody().get("mensaje"));
    }

    @Test
    void verificarUsuarioInexistenteRetorna200() {
        when(usuarioFeignClient.existeUsuario("nuevo@gotystore.com")).thenReturn(false);

        ResponseEntity<Map<String, Object>> response =
                controller.verificar("nuevo@gotystore.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody().get("existe"));
        assertEquals("Usuario no encontrado en el sistema", response.getBody().get("mensaje"));
    }

    @Test
    void verificarFallaRemotaRetorna503() {
        when(usuarioFeignClient.existeUsuario(anyString()))
                .thenThrow(new RuntimeException("Sin conexión"));

        ResponseEntity<Map<String, Object>> response =
                controller.verificar("admin@gotystore.com");

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertTrue(response.getBody().get("error").toString().contains("UsuarioService no disponible"));
    }

    @Test
    void obtenerUsuarioRetorna200() {
        Map<String, Object> usuario = Map.of(
                "email", "admin@gotystore.com",
                "nombre", "Administrador");
        when(usuarioFeignClient.buscarUsuario("admin@gotystore.com")).thenReturn(usuario);

        ResponseEntity<Object> response =
                controller.obtenerUsuario("admin@gotystore.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(usuario, response.getBody());
    }

    @Test
    void obtenerUsuarioFallaRemotaRetorna503() {
        when(usuarioFeignClient.buscarUsuario(anyString()))
                .thenThrow(new RuntimeException("Sin conexión"));

        ResponseEntity<Object> response =
                controller.obtenerUsuario("admin@gotystore.com");

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("UsuarioService no disponible",
                ((Map<?, ?>) response.getBody()).get("error"));
    }
}
