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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    private ClienteService service;

    @BeforeEach
    void setUp() {
        service = new ClienteService(repository);
    }

    @Test
    void guardarClienteValido() {
        ClienteRequestDTO dto = crearDto();
        when(repository.existsByCorreo(dto.getCorreo())).thenReturn(false);
        when(repository.save(any(Cliente.class))).thenAnswer(invocation -> {
            Cliente cliente = invocation.getArgument(0);
            cliente.setId(1);
            return cliente;
        });

        var resultado = service.save(dto);

        assertEquals(1, resultado.getId());
        assertEquals("camila@correo.cl", resultado.getCorreo());
        verify(repository).save(any(Cliente.class));
    }

    @Test
    void rechazarCorreoDuplicado() {
        ClienteRequestDTO dto = crearDto();
        when(repository.existsByCorreo(dto.getCorreo())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.save(dto));

        verify(repository, never()).save(any());
    }

    @Test
    void buscarClienteInexistente() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.findById(99));
    }

    @Test
    void listarClientes() {
        Cliente cliente = crearCliente(1);
        when(repository.findAll()).thenReturn(List.of(cliente));

        var resultado = service.findAll();

        assertEquals(1, resultado.size());
        assertEquals("Camila Rojas", resultado.get(0).getNombre());
        verify(repository).findAll();
    }

    @Test
    void buscarClientePorId() {
        Cliente cliente = crearCliente(1);
        when(repository.findById(1)).thenReturn(Optional.of(cliente));

        var resultado = service.findById(1);

        assertEquals(1, resultado.getId());
        assertEquals("camila@correo.cl", resultado.getCorreo());
    }

    @Test
    void actualizarCliente() {
        Cliente existente = crearCliente(1);
        ClienteRequestDTO dto = crearDto();
        dto.setNombre("Camila Actualizada");
        when(repository.findById(1)).thenReturn(Optional.of(existente));
        when(repository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = service.update(1, dto);

        assertEquals(1, resultado.getId());
        assertEquals("Camila Actualizada", resultado.getNombre());
        verify(repository).save(argThat(cliente -> cliente.getId() == 1));
    }

    @Test
    void eliminarClienteExistente() {
        when(repository.findById(1)).thenReturn(Optional.of(crearCliente(1)));

        service.deleteById(1);

        verify(repository).deleteById(1);
    }

    private ClienteRequestDTO crearDto() {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNombre("Camila Rojas");
        dto.setCorreo("camila@correo.cl");
        dto.setDireccion("Av. Principal 123");
        dto.setTelefono("987654321");
        return dto;
    }

    private Cliente crearCliente(int id) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNombre("Camila Rojas");
        cliente.setCorreo("camila@correo.cl");
        cliente.setDireccion("Av. Principal 123");
        cliente.setTelefono("987654321");
        return cliente;
    }
}
