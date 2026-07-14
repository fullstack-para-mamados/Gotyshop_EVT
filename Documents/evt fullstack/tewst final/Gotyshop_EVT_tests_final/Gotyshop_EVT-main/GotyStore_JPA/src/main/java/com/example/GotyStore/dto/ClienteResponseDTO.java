package com.example.GotyStore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representación de un cliente registrado en la tienda")
public class ClienteResponseDTO {

    @Schema(description = "Identificador único del cliente", example = "1")
    private int id;

    @Schema(description = "Nombre completo del cliente", example = "Camila Rojas")
    private String nombre;

    @Schema(description = "Correo electrónico único del cliente", example = "camila.rojas@correo.com")
    private String correo;

    @Schema(description = "Dirección de despacho del cliente", example = "Av. Siempre Viva 742, Santiago")
    private String direccion;

    @Schema(description = "Teléfono de contacto del cliente", example = "+56912345678")
    private String telefono;
}
