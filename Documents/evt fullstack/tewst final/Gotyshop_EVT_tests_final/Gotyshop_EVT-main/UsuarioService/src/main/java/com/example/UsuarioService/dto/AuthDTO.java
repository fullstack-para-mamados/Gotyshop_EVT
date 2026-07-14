package com.example.UsuarioService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class AuthDTO {

    @Data
    public static class LoginRequest {
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Formato de email inválido")
        private String email;

        @NotBlank(message = "La contraseña es obligatoria")
        private String password;
    }

    @Data
    public static class LoginResponse {
        private String token;
        private String email;
        private String nombre;
        private String rol;

        public LoginResponse(String token, String email, String nombre, String rol) {
            this.token = token;
            this.email = email;
            this.nombre = nombre;
            this.rol = rol;
        }
    }

    @Data
    public static class UsuarioResponse {
        private int id;
        private String nombre;
        private String email;
        private String rol;
        private boolean activo;

        public UsuarioResponse(int id, String nombre, String email, String rol, boolean activo) {
            this.id = id;
            this.nombre = nombre;
            this.email = email;
            this.rol = rol;
            this.activo = activo;
        }
    }
}
