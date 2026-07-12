package com.example.GotyStore.service;

import com.example.GotyStore.Model.Cliente;
import com.example.GotyStore.Model.Juego;
import com.example.GotyStore.Model.Venta;
import com.example.GotyStore.dto.VentaRequestDTO;
import com.example.GotyStore.repository.VentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {
    @Mock VentaRepository repository;
    @Mock ClienteService clienteService;
    @Mock JuegoService juegoService;
    VentaService service;
    @BeforeEach void setUp() { service = new VentaService(repository, clienteService, juegoService); }

    @Test void registrarVentaValida() {
        Cliente c = new Cliente(); c.setId(1); c.setNombre("Camila");
        Juego j = new Juego(); j.setId(2); j.setTitulo("Hades");
        VentaRequestDTO dto = new VentaRequestDTO();
        dto.setFecha("2026-07-11"); dto.setIdCliente(1); dto.setIdJuego(2); dto.setCantidad(2); dto.setTotal(30000);
        when(clienteService.findEntityById(1)).thenReturn(c);
        when(juegoService.findEntityById(2)).thenReturn(j);
        when(repository.save(any(Venta.class))).thenAnswer(i -> { Venta v=i.getArgument(0); v.setId(10); return v; });
        var result = service.save(dto);
        assertEquals(10, result.getId()); assertEquals(30000, result.getTotal());
    }

    @Test void rechazarCantidadCero() {
        VentaRequestDTO dto = new VentaRequestDTO(); dto.setCantidad(0);
        assertThrows(IllegalArgumentException.class, () -> service.save(dto));
        verifyNoInteractions(repository, clienteService, juegoService);
    }
}
