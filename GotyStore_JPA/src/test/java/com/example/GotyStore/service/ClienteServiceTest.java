package com.example.GotyStore.service;

import com.example.GotyStore.Model.Cliente;
import com.example.GotyStore.dto.ClienteRequestDTO;
import com.example.GotyStore.exception.RecursoNoEncontradoException;
import com.example.GotyStore.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {
    @Mock ClienteRepository repository;
    ClienteService service;

    @BeforeEach void setUp() { service = new ClienteService(repository); }

    @Test void guardarClienteValido() {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNombre("Camila Rojas"); dto.setCorreo("camila@correo.cl");
        when(repository.existsByCorreo(dto.getCorreo())).thenReturn(false);
        when(repository.save(any(Cliente.class))).thenAnswer(i -> { Cliente c=i.getArgument(0); c.setId(1); return c; });
        var result = service.save(dto);
        assertEquals(1, result.getId());
        assertEquals("camila@correo.cl", result.getCorreo());
    }

    @Test void rechazarCorreoDuplicado() {
        ClienteRequestDTO dto = new ClienteRequestDTO(); dto.setCorreo("repetido@correo.cl");
        when(repository.existsByCorreo(dto.getCorreo())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.save(dto));
        verify(repository, never()).save(any());
    }

    @Test void buscarClienteInexistente() {
        when(repository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.findById(99));
    }
}
