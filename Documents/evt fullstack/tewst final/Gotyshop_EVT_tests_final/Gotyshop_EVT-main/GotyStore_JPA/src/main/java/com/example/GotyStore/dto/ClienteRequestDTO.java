package com.example.GotyStore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
@Schema(description = "Datos requeridos para crear o actualizar un cliente")
public class ClienteRequestDTO {

    @Schema(description = "Nombre completo del cliente", example = "Camila Rojas")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Schema(description = "Correo electrónico único del cliente", example = "camila.rojas@correo.com")
    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El correo no tiene formato válido")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
    private String correo;

    @Schema(description = "Dirección de despacho del cliente", example = "Av. Siempre Viva 742, Santiago")
    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres")
    private String direccion;

    @Schema(description = "Teléfono de contacto, solo números, espacios o signo +", example = "+56912345678")
    @Pattern(regexp = "^$|^[+0-9 ]{8,20}$", message = "El teléfono debe contener solo números, espacios o signo +")
    private String telefono;
}
