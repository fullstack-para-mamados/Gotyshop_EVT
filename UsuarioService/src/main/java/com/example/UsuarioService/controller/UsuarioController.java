package com.example.UsuarioService.controller;

import com.example.UsuarioService.Model.Usuario;
import com.example.UsuarioService.dto.AuthDTO;
import com.example.UsuarioService.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Usuarios", description = "Autenticación JWT y administración de usuarios de GotyShop")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioService usuarioService;

    @Operation(summary = "Login", description = "Autentica un usuario y retorna un token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso, retorna token JWT"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    @PostMapping("/api/auth/login")
    public ResponseEntity<AuthDTO.LoginResponse> login(@Valid @RequestBody AuthDTO.LoginRequest request) {
        log.info("Petición de login recibida para: {}", request.getEmail());
        return ResponseEntity.ok(usuarioService.login(request));
    }

    @Operation(summary = "Registro", description = "Registra un nuevo usuario en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado")
    })
    @PostMapping("/api/auth/registro")
    public ResponseEntity<Usuario> registro(@Valid @RequestBody Usuario usuario) {
        log.info("Petición de registro recibida para: {}", usuario.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrar(usuario));
    }

    @Operation(summary = "Listar usuarios", description = "Retorna la lista completa de usuarios registrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios"),
            @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    })
    @GetMapping("/api/usuarios")
    public ResponseEntity<List<Usuario>> getAll() {
        log.info("Listando todos los usuarios");
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @Operation(summary = "Buscar usuario por ID", description = "Retorna un usuario según su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    })
    @GetMapping("/api/usuarios/{id}")
    public ResponseEntity<Usuario> getById(
            @Parameter(description = "ID del usuario", required = true) @PathVariable int id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @Operation(summary = "Verificar email", description = "Verifica si un email ya está registrado en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retorna true si existe, false si no")
    })
    @GetMapping("/api/usuarios/existe")
    public ResponseEntity<Boolean> existe(
            @Parameter(description = "Email a verificar", required = true) @RequestParam String email) {
        log.info("Verificando existencia del usuario: {}", email);
        return ResponseEntity.ok(usuarioService.existsByEmail(email));
    }

    @Operation(summary = "Buscar usuario por email", description = "Retorna los datos de un usuario según su email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/api/usuarios/buscar")
    public ResponseEntity<Usuario> buscarPorEmail(
            @Parameter(description = "Email del usuario", required = true) @RequestParam String email) {
        return ResponseEntity.ok(usuarioService.findByEmail(email));
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    })
    @DeleteMapping("/api/usuarios/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del usuario a eliminar", required = true) @PathVariable int id) {
        log.info("Petición de eliminación del usuario con id: {}", id);
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}