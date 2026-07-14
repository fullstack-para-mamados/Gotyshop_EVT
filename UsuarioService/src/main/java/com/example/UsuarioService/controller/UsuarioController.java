package com.example.UsuarioService.controller;

import com.example.UsuarioService.Model.Usuario;
import com.example.UsuarioService.dto.AuthDTO;
import com.example.UsuarioService.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioService usuarioService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<AuthDTO.LoginResponse> login(@Valid @RequestBody AuthDTO.LoginRequest request) {
        log.info("Petición de login recibida para: {}", request.getEmail());
        return ResponseEntity.ok(usuarioService.login(request));
    }

    @PostMapping("/api/auth/registro")
    public ResponseEntity<Usuario> registro(@Valid @RequestBody Usuario usuario) {
        log.info("Petición de registro recibida para: {}", usuario.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrar(usuario));
    }

    @GetMapping("/api/usuarios")
    public ResponseEntity<List<Usuario>> getAll() {
        log.info("Listando todos los usuarios");
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/api/usuarios/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable int id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @GetMapping("/api/usuarios/existe")
    public ResponseEntity<Boolean> existe(@RequestParam String email) {
        log.info("Verificando existencia del usuario: {}", email);
        return ResponseEntity.ok(usuarioService.existsByEmail(email));
    }

    @GetMapping("/api/usuarios/buscar")
    public ResponseEntity<Usuario> buscarPorEmail(@RequestParam String email) {
        return ResponseEntity.ok(usuarioService.findByEmail(email));
    }

    @DeleteMapping("/api/usuarios/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        log.info("Petición de eliminación del usuario con id: {}", id);
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
