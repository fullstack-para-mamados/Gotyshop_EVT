package com.example.GotyStore.service;

import com.example.GotyStore.Model.Cliente;
import com.example.GotyStore.Model.Juego;
import com.example.GotyStore.Model.Venta;
import com.example.GotyStore.dto.VentaRequestDTO;
import com.example.GotyStore.exception.RecursoNoEncontradoException;
import com.example.GotyStore.repository.VentaRepository;
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
class VentaServiceTest {

    @Mock
    private VentaRepository repository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private JuegoService juegoService;

    private VentaService service;

    @BeforeEach
    void setUp() {
        service = new VentaService(repository, clienteService, juegoService);
    }

    @Test
    void registrarVentaValida() {
        Cliente cliente = crearCliente();
        Juego juego = crearJuego();
        VentaRequestDTO dto = crearDto();
        when(clienteService.findEntityById(1)).thenReturn(cliente);
        when(juegoService.findEntityById(2)).thenReturn(juego);
        when(repository.save(any(Venta.class))).thenAnswer(invocation -> {
            Venta venta = invocation.getArgument(0);
            venta.setId(10);
            return venta;
        });

        var resultado = service.save(dto);

        assertEquals(10, resultado.getId());
        assertEquals(30000.0, resultado.getTotal());
        assertEquals("Camila", resultado.getNombreCliente());
        assertEquals("Hades", resultado.getTituloJuego());
    }

    @Test
    void rechazarCantidadCero() {
        VentaRequestDTO dto = new VentaRequestDTO();
        dto.setCantidad(0);

        assertThrows(IllegalArgumentException.class, () -> service.save(dto));

        verifyNoInteractions(repository, clienteService, juegoService);
    }

    @Test
    void listarVentas() {
        when(repository.findAll()).thenReturn(List.of(crearVenta(10)));

        var resultado = service.findAll();

        assertEquals(1, resultado.size());
        assertEquals(10, resultado.get(0).getId());
        verify(repository).findAll();
    }

    @Test
    void buscarVentaPorId() {
        when(repository.findById(10)).thenReturn(Optional.of(crearVenta(10)));

        var resultado = service.findById(10);

        assertEquals(10, resultado.getId());
        assertEquals(1, resultado.getIdCliente());
        assertEquals(2, resultado.getIdJuego());
    }

    @Test
    void buscarVentaInexistente() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.findById(99));
    }

    @Test
    void listarVentasPorCliente() {
        when(repository.findByClienteId(1)).thenReturn(List.of(crearVenta(10)));

        var resultado = service.findByClienteId(1);

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdCliente());
        verify(repository).findByClienteId(1);
    }

    @Test
    void listarVentasPorJuego() {
        when(repository.findByJuegoId(2)).thenReturn(List.of(crearVenta(10)));

        var resultado = service.findByJuegoId(2);

        assertEquals(1, resultado.size());
        assertEquals(2, resultado.get(0).getIdJuego());
        verify(repository).findByJuegoId(2);
    }

    @Test
    void eliminarVentaExistente() {
        when(repository.findById(10)).thenReturn(Optional.of(crearVenta(10)));

        service.deleteById(10);

        verify(repository).deleteById(10);
    }

    @Test
    void eliminarVentaInexistente() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.deleteById(99));

        verify(repository, never()).deleteById(anyInt());
    }

    private VentaRequestDTO crearDto() {
        VentaRequestDTO dto = new VentaRequestDTO();
        dto.setFecha("2026-07-11");
        dto.setIdCliente(1);
        dto.setIdJuego(2);
        dto.setCantidad(2);
        dto.setTotal(30000);
        return dto;
    }

    private Venta crearVenta(int id) {
        Venta venta = new Venta();
        venta.setId(id);
        venta.setFecha("2026-07-11");
        venta.setCliente(crearCliente());
        venta.setJuego(crearJuego());
        venta.setCantidad(2);
        venta.setTotal(30000);
        return venta;
    }

    private Cliente crearCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNombre("Camila");
        return cliente;
    }

    private Juego crearJuego() {
        Juego juego = new Juego();
        juego.setId(2);
        juego.setTitulo("Hades");
        return juego;
    }
}
