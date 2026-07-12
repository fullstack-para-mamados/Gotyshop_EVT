package com.example.GotyStore.controller;

import com.example.GotyStore.service.UsuarioFeignClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Operaciones de verificación de usuarios contra UsuarioService")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UsuarioFeignClient usuarioFeignClient;

    @GetMapping("/verificar")
    @Operation(summary = "Verificar si un usuario existe", description = "Consulta a UsuarioService si el email pertenece a un usuario registrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente"),
            @ApiResponse(responseCode = "503", description = "UsuarioService no disponible")
    })
    public ResponseEntity<Map<String, Object>> verificar(
            @Parameter(description = "Email del usuario a verificar", required = true) @RequestParam String email) {
        log.info("Verificando usuario: {} contra UsuarioService", email);
        try {
            Boolean existe = usuarioFeignClient.existeUsuario(email);
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("email", email);
            respuesta.put("existe", existe);
            respuesta.put("mensaje", Boolean.TRUE.equals(existe)
                    ? "Usuario encontrado en el sistema"
                    : "Usuario no encontrado en el sistema");
            log.info("Resultado verificación {}: {}", email, existe);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            log.error("Error al contactar UsuarioService: {}", e.getMessage());
            return ResponseEntity.status(503).body(Map.of(
                    "error", "UsuarioService no disponible: " + e.getMessage()));
        }
    }

    @GetMapping("/usuario")
    @Operation(summary = "Obtener datos de un usuario", description = "Obtiene la información de un usuario desde UsuarioService según su email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario obtenido exitosamente"),
            @ApiResponse(responseCode = "503", description = "UsuarioService no disponible")
    })
    public ResponseEntity<Object> obtenerUsuario(
            @Parameter(description = "Email del usuario a buscar", required = true) @RequestParam String email) {
        log.info("Obteniendo datos del usuario: {} desde UsuarioService", email);
        try {
            Map<String, Object> usuario = usuarioFeignClient.buscarUsuario(email);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            log.error("Error al obtener usuario de UsuarioService: {}", e.getMessage());
            return ResponseEntity.status(503).body(Map.of(
                    "error", "UsuarioService no disponible"));
        }
    }
}