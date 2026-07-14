package com.example.UsuarioService.service;

import com.example.UsuarioService.Model.Usuario;
import com.example.UsuarioService.dto.AuthDTO;
import com.example.UsuarioService.exception.RecursoNoEncontradoException;
import com.example.UsuarioService.repository.UsuarioRepository;
import com.example.UsuarioService.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Administrador");
        usuario.setEmail("admin@gotystore.com");
        usuario.setPassword("passwordCodificada");
        usuario.setRol("ADMIN");
    }

    @Test
    void findAll_deberiaRetornarListaDeUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.findAll();

        assertEquals(1, resultado.size());
        assertEquals("admin@gotystore.com", resultado.get(0).getEmail());

        verify(usuarioRepository).findAll();
    }

    @Test
    void findById_deberiaRetornarUsuarioCuandoExiste() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.findById(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Administrador", resultado.getNombre());

        verify(usuarioRepository).findById(1);
    }

    @Test
    void findById_deberiaLanzarExcepcionCuandoNoExiste() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        RecursoNoEncontradoException excepcion = assertThrows(
                RecursoNoEncontradoException.class,
                () -> usuarioService.findById(99)
        );

        assertEquals("Usuario con id 99 no encontrado", excepcion.getMessage());

        verify(usuarioRepository).findById(99);
    }

    @Test
    void findByEmail_deberiaRetornarUsuarioCuandoExiste() {
        when(usuarioRepository.findByEmail("admin@gotystore.com"))
                .thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.findByEmail("admin@gotystore.com");

        assertNotNull(resultado);
        assertEquals("admin@gotystore.com", resultado.getEmail());

        verify(usuarioRepository).findByEmail("admin@gotystore.com");
    }

    @Test
    void findByEmail_deberiaLanzarExcepcionCuandoNoExiste() {
        when(usuarioRepository.findByEmail("noexiste@gotystore.com"))
                .thenReturn(Optional.empty());

        RecursoNoEncontradoException excepcion = assertThrows(
                RecursoNoEncontradoException.class,
                () -> usuarioService.findByEmail("noexiste@gotystore.com")
        );

        assertEquals(
                "Usuario con email noexiste@gotystore.com no encontrado",
                excepcion.getMessage()
        );

        verify(usuarioRepository).findByEmail("noexiste@gotystore.com");
    }

    @Test
    void existsByEmail_deberiaRetornarTrueCuandoExiste() {
        when(usuarioRepository.existsByEmail("admin@gotystore.com"))
                .thenReturn(true);

        boolean resultado = usuarioService.existsByEmail("admin@gotystore.com");

        assertTrue(resultado);

        verify(usuarioRepository).existsByEmail("admin@gotystore.com");
    }

    @Test
    void existsByEmail_deberiaRetornarFalseCuandoNoExiste() {
        when(usuarioRepository.existsByEmail("nuevo@gotystore.com"))
                .thenReturn(false);

        boolean resultado = usuarioService.existsByEmail("nuevo@gotystore.com");

        assertFalse(resultado);

        verify(usuarioRepository).existsByEmail("nuevo@gotystore.com");
    }

    @Test
    void registrar_deberiaGuardarUsuarioCuandoElEmailNoExiste() {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Gabriel");
        nuevoUsuario.setEmail("gabriel@gotystore.com");
        nuevoUsuario.setPassword("password123");
        nuevoUsuario.setRol("USER");

        when(usuarioRepository.existsByEmail("gabriel@gotystore.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("passwordCodificada");

        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> {
                    Usuario guardado = invocation.getArgument(0);
                    guardado.setId(2);
                    return guardado;
                });

        Usuario resultado = usuarioService.registrar(nuevoUsuario);

        assertNotNull(resultado);
        assertEquals(2, resultado.getId());
        assertEquals("passwordCodificada", resultado.getPassword());

        verify(usuarioRepository).existsByEmail("gabriel@gotystore.com");
        verify(passwordEncoder).encode("password123");
        verify(usuarioRepository).save(nuevoUsuario);
    }

    @Test
    void registrar_deberiaLanzarExcepcionCuandoElEmailYaExiste() {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail("admin@gotystore.com");
        nuevoUsuario.setPassword("password123");

        when(usuarioRepository.existsByEmail("admin@gotystore.com"))
                .thenReturn(true);

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.registrar(nuevoUsuario)
        );

        assertEquals(
                "Ya existe un usuario con el email: admin@gotystore.com",
                excepcion.getMessage()
        );

        verify(usuarioRepository).existsByEmail("admin@gotystore.com");
        verify(usuarioRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void login_deberiaRetornarTokenCuandoLasCredencialesSonCorrectas() {
        AuthDTO.LoginRequest request = new AuthDTO.LoginRequest();
        request.setEmail("admin@gotystore.com");
        request.setPassword("password123");

        when(usuarioRepository.findByEmail("admin@gotystore.com"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches("password123", "passwordCodificada"))
                .thenReturn(true);

        when(jwtUtil.generateToken("admin@gotystore.com", "ADMIN"))
                .thenReturn("token-jwt-prueba");

        AuthDTO.LoginResponse resultado = usuarioService.login(request);

        assertNotNull(resultado);
        assertEquals("token-jwt-prueba", resultado.getToken());
        assertEquals("admin@gotystore.com", resultado.getEmail());
        assertEquals("Administrador", resultado.getNombre());
        assertEquals("ADMIN", resultado.getRol());

        verify(usuarioRepository).findByEmail("admin@gotystore.com");
        verify(passwordEncoder).matches("password123", "passwordCodificada");
        verify(jwtUtil).generateToken("admin@gotystore.com", "ADMIN");
    }

    @Test
    void login_deberiaLanzarExcepcionCuandoElEmailNoExiste() {
        AuthDTO.LoginRequest request = new AuthDTO.LoginRequest();
        request.setEmail("noexiste@gotystore.com");
        request.setPassword("password123");

        when(usuarioRepository.findByEmail("noexiste@gotystore.com"))
                .thenReturn(Optional.empty());

        RecursoNoEncontradoException excepcion = assertThrows(
                RecursoNoEncontradoException.class,
                () -> usuarioService.login(request)
        );

        assertEquals("Credenciales inválidas", excepcion.getMessage());

        verify(usuarioRepository).findByEmail("noexiste@gotystore.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }

    @Test
    void login_deberiaLanzarExcepcionCuandoLaPasswordEsIncorrecta() {
        AuthDTO.LoginRequest request = new AuthDTO.LoginRequest();
        request.setEmail("admin@gotystore.com");
        request.setPassword("incorrecta");

        when(usuarioRepository.findByEmail("admin@gotystore.com"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches("incorrecta", "passwordCodificada"))
                .thenReturn(false);

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.login(request)
        );

        assertEquals("Credenciales inválidas", excepcion.getMessage());

        verify(usuarioRepository).findByEmail("admin@gotystore.com");
        verify(passwordEncoder).matches("incorrecta", "passwordCodificada");
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }

    @Test
    void deleteById_deberiaEliminarUsuarioCuandoExiste() {
        when(usuarioRepository.existsById(1)).thenReturn(true);

        usuarioService.deleteById(1);

        verify(usuarioRepository).existsById(1);
        verify(usuarioRepository).deleteById(1);
    }

    @Test
    void deleteById_deberiaLanzarExcepcionCuandoNoExiste() {
        when(usuarioRepository.existsById(99)).thenReturn(false);

        RecursoNoEncontradoException excepcion = assertThrows(
                RecursoNoEncontradoException.class,
                () -> usuarioService.deleteById(99)
        );

        assertEquals("Usuario con id 99 no encontrado", excepcion.getMessage());

        verify(usuarioRepository).existsById(99);
        verify(usuarioRepository, never()).deleteById(anyInt());
    }
}