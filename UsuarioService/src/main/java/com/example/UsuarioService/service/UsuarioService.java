package com.example.UsuarioService.service;

import com.example.UsuarioService.Model.Usuario;
import com.example.UsuarioService.dto.AuthDTO;
import com.example.UsuarioService.exception.RecursoNoEncontradoException;
import com.example.UsuarioService.repository.UsuarioRepository;
import com.example.UsuarioService.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public List<Usuario> findAll() {
        log.info("Consultando todos los usuarios");
        return usuarioRepository.findAll();
    }

    public Usuario findById(int id) {
        log.info("Buscando usuario con id: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario con id {} no encontrado", id);
                    return new RecursoNoEncontradoException("Usuario con id " + id + " no encontrado");
                });
    }

    public Usuario findByEmail(String email) {
        log.info("Buscando usuario con email: {}", email);
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Usuario con email {} no encontrado", email);
                    return new RecursoNoEncontradoException("Usuario con email " + email + " no encontrado");
                });
    }

    public boolean existsByEmail(String email) {
        boolean existe = usuarioRepository.existsByEmail(email);
        log.debug("Verificando existencia de email {}: {}", email, existe);
        return existe;
    }

    public Usuario registrar(Usuario usuario) {
        log.info("Registrando nuevo usuario: {}", usuario.getEmail());
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            log.warn("Intento de registro con email duplicado: {}", usuario.getEmail());
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuario.getEmail());
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario registrado exitosamente con id: {}", guardado.getId());
        return guardado;
    }

    public AuthDTO.LoginResponse login(AuthDTO.LoginRequest request) {
        log.info("Intento de login para: {}", request.getEmail());
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login fallido - email no encontrado: {}", request.getEmail());
                    return new RecursoNoEncontradoException("Credenciales inválidas");
                });

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            log.warn("Login fallido - contraseña incorrecta para: {}", request.getEmail());
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol());
        log.info("Login exitoso para: {}", usuario.getEmail());
        return new AuthDTO.LoginResponse(token, usuario.getEmail(), usuario.getNombre(), usuario.getRol());
    }

    public void deleteById(int id) {
        log.info("Eliminando usuario con id: {}", id);
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario con id " + id + " no encontrado");
        }
        usuarioRepository.deleteById(id);
        log.info("Usuario con id {} eliminado correctamente", id);
    }
}
