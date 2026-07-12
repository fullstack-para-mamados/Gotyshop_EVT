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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JuegoServiceTest {
    @Mock JuegoRepository repository;
    JuegoService service;
    @BeforeEach void setUp() { service = new JuegoService(repository); }

    @Test void guardarJuegoValido() {
        JuegoRequestDTO dto = new JuegoRequestDTO();
        dto.setTitulo("Hades"); dto.setDesarrollador("Supergiant"); dto.setPlataforma("PC");
        when(repository.save(any(Juego.class))).thenAnswer(i -> { Juego j=i.getArgument(0); j.setId(7); return j; });
        assertEquals(7, service.save(dto).getId());
    }

    @Test void filtrarPorPlataforma() {
        Juego juego = new Juego(); juego.setId(1); juego.setTitulo("Hades"); juego.setPlataforma("PC");
        when(repository.findByPlataforma("PC")).thenReturn(List.of(juego));
        assertEquals(1, service.findByPlataforma("PC").size());
    }

    @Test void eliminarJuegoInexistente() {
        when(repository.findById(5)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.deleteById(5));
        verify(repository, never()).deleteById(anyInt());
    }
}
