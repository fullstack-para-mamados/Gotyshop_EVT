package com.example.GotyStore.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "juego")
@Data @AllArgsConstructor @NoArgsConstructor
public class Juego {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 2, max = 100, message = "El título debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String titulo;

    @NotBlank(message = "El desarrollador no puede estar vacío")
    @Size(min = 2, max = 100, message = "El desarrollador debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String desarrollador;

    @NotBlank(message = "La plataforma no puede estar vacía")
    @Size(min = 2, max = 50, message = "La plataforma debe tener entre 2 y 50 caracteres")
    @Column(nullable = false, length = 50)
    private String plataforma;

    @Pattern(regexp = "^(19|20)\\d{2}$", message = "El año debe tener formato YYYY")
    @Column(name = "anio_lanzamiento", length = 4)
    private String anioLanzamiento;

    @Column(length = 50)
    private String genero;

    @JsonIgnore
    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Venta> ventas;
}
