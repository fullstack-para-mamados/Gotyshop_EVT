package com.example.GotyStore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
@Schema(description = "Datos requeridos para registrar una venta")
public class VentaRequestDTO {

    @Schema(description = "Fecha de la venta en formato yyyy-MM-dd", example = "2026-06-15")
    @NotBlank(message = "La fecha no puede estar vacía")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "La fecha debe tener formato yyyy-MM-dd")
    private String fecha;

    @Schema(description = "Identificador del cliente que realiza la compra", example = "1")
    @NotNull(message = "El cliente es obligatorio")
    @Min(value = 1, message = "El id del cliente debe ser válido")
    private Integer idCliente;

    @Schema(description = "Identificador del juego vendido", example = "3")
    @NotNull(message = "El juego es obligatorio")
    @Min(value = 1, message = "El id del juego debe ser válido")
    private Integer idJuego;

    @Schema(description = "Cantidad de unidades vendidas", example = "2")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int cantidad;

    @Schema(description = "Monto total de la venta en pesos chilenos", example = "39980")
    @Min(value = 0, message = "El total no puede ser negativo")
    private double total;
}
