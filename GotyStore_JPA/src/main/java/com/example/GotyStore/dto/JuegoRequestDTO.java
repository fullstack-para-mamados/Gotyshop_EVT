package com.example.GotyStore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos requeridos para crear o actualizar un juego")
public class JuegoRequestDTO {

    @Schema(description = "Título del videojuego", example = "The Legend of Zelda: Tears of the Kingdom")
    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 2, max = 100, message = "El título debe tener entre 2 y 100 caracteres")
    private String titulo;

    @Schema(description = "Estudio o empresa desarrolladora del juego", example = "Nintendo EPD")
    @NotBlank(message = "El desarrollador no puede estar vacío")
    @Size(min = 2, max = 100, message = "El desarrollador debe tener entre 2 y 100 caracteres")
    private String desarrollador;

    @Schema(description = "Plataforma en la que está disponible el juego", example = "Nintendo Switch")
    @NotBlank(message = "La plataforma no puede estar vacía")
    @Size(min = 2, max = 50, message = "La plataforma debe tener entre 2 y 50 caracteres")
    private String plataforma;

    @Schema(description = "Año de lanzamiento en formato YYYY", example = "2023")
    @Pattern(regexp = "^(19|20)\\d{2}$", message = "El año debe tener formato YYYY")
    private String anioLanzamiento;

    @Schema(description = "Género principal del juego", example = "Aventura")
    @Size(max = 50, message = "El género no puede superar los 50 caracteres")
    private String genero;
}
