package com.example.GotyStore.service;

import com.example.GotyStore.Model.Juego;
import com.example.GotyStore.dto.JuegoRequestDTO;
import com.example.GotyStore.exception.RecursoNoEncontradoException;
import com.example.GotyStore.repository.JuegoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JuegoServiceTest {

    @Mock
    private JuegoRepository repository;

    private JuegoService service;

    @BeforeEach
    void setUp() {
        service = new JuegoService(repository);
    }

    @Test
    void guardarJuegoValido() {
        JuegoRequestDTO dto = crearDto();
        when(repository.save(any(Juego.class))).thenAnswer(invocation -> {
            Juego juego = invocation.getArgument(0);
            juego.setId(7);
            return juego;
        });

        var resultado = service.save(dto);

        assertEquals(7, resultado.getId());
        assertEquals("Hades", resultado.getTitulo());
    }

    @Test
    void filtrarPorPlataforma() {
        Juego juego = crearJuego(1);
        when(repository.findByPlataforma("PC")).thenReturn(List.of(juego));

        var resultado = service.findByPlataforma("PC");

        assertEquals(1, resultado.size());
        assertEquals("PC", resultado.get(0).getPlataforma());
    }

    @Test
    void eliminarJuegoInexistente() {
        when(repository.findById(5)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.deleteById(5));

        verify(repository, never()).deleteById(anyInt());
    }

    @Test
    void listarJuegos() {
        when(repository.findAll()).thenReturn(List.of(crearJuego(1)));

        var resultado = service.findAll();

        assertEquals(1, resultado.size());
        assertEquals("Hades", resultado.get(0).getTitulo());
        verify(repository).findAll();
    }

    @Test
    void buscarJuegoPorId() {
        when(repository.findById(1)).thenReturn(Optional.of(crearJuego(1)));

        var resultado = service.findById(1);

        assertEquals(1, resultado.getId());
        assertEquals("Supergiant", resultado.getDesarrollador());
    }

    @Test
    void buscarJuegoInexistente() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.findById(99));
    }

    @Test
    void actualizarJuego() {
        JuegoRequestDTO dto = crearDto();
        dto.setTitulo("Hades II");
        when(repository.findById(1)).thenReturn(Optional.of(crearJuego(1)));
        when(repository.save(any(Juego.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = service.update(1, dto);

        assertEquals(1, resultado.getId());
        assertEquals("Hades II", resultado.getTitulo());
        verify(repository).save(argThat(juego -> juego.getId() == 1));
    }

    @Test
    void eliminarJuegoExistente() {
        when(repository.findById(1)).thenReturn(Optional.of(crearJuego(1)));

        service.deleteById(1);

        verify(repository).deleteById(1);
    }

    private JuegoRequestDTO crearDto() {
        JuegoRequestDTO dto = new JuegoRequestDTO();
        dto.setTitulo("Hades");
        dto.setDesarrollador("Supergiant");
        dto.setPlataforma("PC");
        dto.setAnioLanzamiento("2020");
        dto.setGenero("Roguelike");
        return dto;
    }

    private Juego crearJuego(int id) {
        Juego juego = new Juego();
        juego.setId(id);
        juego.setTitulo("Hades");
        juego.setDesarrollador("Supergiant");
        juego.setPlataforma("PC");
        juego.setAnioLanzamiento("2020");
        juego.setGenero("Roguelike");
        return juego;
    }
}
