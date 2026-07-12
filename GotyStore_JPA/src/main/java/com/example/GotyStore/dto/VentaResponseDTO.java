package com.example.GotyStore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representación de una venta registrada")
public class VentaResponseDTO {

    @Schema(description = "Identificador único de la venta", example = "1")
    private int id;

    @Schema(description = "Fecha de la venta en formato yyyy-MM-dd", example = "2026-06-15")
    private String fecha;

    @Schema(description = "Identificador del cliente que realizó la compra", example = "1")
    private int idCliente;

    @Schema(description = "Nombre del cliente que realizó la compra", example = "Camila Rojas")
    private String nombreCliente;

    @Schema(description = "Identificador del juego vendido", example = "3")
    private int idJuego;

    @Schema(description = "Título del juego vendido", example = "The Legend of Zelda: Tears of the Kingdom")
    private String tituloJuego;

    @Schema(description = "Cantidad de unidades vendidas", example = "2")
    private int cantidad;

    @Schema(description = "Monto total de la venta en pesos chilenos", example = "39980")
    private double total;
}
