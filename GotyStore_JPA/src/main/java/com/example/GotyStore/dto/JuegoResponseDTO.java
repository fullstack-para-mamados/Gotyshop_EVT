package com.example.GotyStore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representación de un juego disponible en la tienda")
public class JuegoResponseDTO {

    @Schema(description = "Identificador único del juego", example = "1")
    private int id;

    @Schema(description = "Título del videojuego", example = "The Legend of Zelda: Tears of the Kingdom")
    private String titulo;

    @Schema(description = "Estudio o empresa desarrolladora del juego", example = "Nintendo EPD")
    private String desarrollador;

    @Schema(description = "Plataforma en la que está disponible el juego", example = "Nintendo Switch")
    private String plataforma;

    @Schema(description = "Año de lanzamiento en formato YYYY", example = "2023")
    private String anioLanzamiento;

    @Schema(description = "Género principal del juego", example = "Aventura")
    private String genero;
}
