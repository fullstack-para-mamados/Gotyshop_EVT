package com.example.GotyStore.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cliente")
@Data @AllArgsConstructor @NoArgsConstructor
public class Cliente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El correo no tiene formato válido")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100, unique = true)
    private String correo;

    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres")
    @Column(length = 200)
    private String direccion;

    @Pattern(regexp = "^$|^[+0-9 ]{8,20}$", message = "El teléfono debe contener solo números, espacios o signo +")
    @Column(length = 20)
    private String telefono;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Venta> ventas;
}
