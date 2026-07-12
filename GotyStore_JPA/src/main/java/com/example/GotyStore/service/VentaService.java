package com.example.GotyStore.service;

import com.example.GotyStore.Model.Cliente;
import com.example.GotyStore.Model.Juego;
import com.example.GotyStore.Model.Venta;
import com.example.GotyStore.dto.VentaRequestDTO;
import com.example.GotyStore.dto.VentaResponseDTO;
import com.example.GotyStore.exception.RecursoNoEncontradoException;
import com.example.GotyStore.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaService {

    private static final Logger log = LoggerFactory.getLogger(VentaService.class);
    private final VentaRepository ventaRepository;
    private final ClienteService clienteService;
    private final JuegoService juegoService;

    public List<VentaResponseDTO> findAll() {
        log.info("Listando todas las ventas");
        return ventaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public VentaResponseDTO findById(int id) {
        return toResponseDTO(findEntityById(id));
    }

    private Venta findEntityById(int id) {
        log.info("Buscando venta con id: {}", id);
        return ventaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Venta con id {} no encontrada", id);
                    return new RecursoNoEncontradoException("Venta con id " + id + " no encontrada");
                });
    }

    public List<VentaResponseDTO> findByClienteId(int clienteId) {
        log.info("Listando ventas del cliente: {}", clienteId);
        return ventaRepository.findByClienteId(clienteId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<VentaResponseDTO> findByJuegoId(int juegoId) {
        log.info("Listando ventas del juego: {}", juegoId);
        return ventaRepository.findByJuegoId(juegoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public VentaResponseDTO save(VentaRequestDTO dto) {
        if (dto.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        // findEntityById ya lanza RecursoNoEncontradoException (-> 404) si no existen
        Cliente cliente = clienteService.findEntityById(dto.getIdCliente());
        Juego juego = juegoService.findEntityById(dto.getIdJuego());

        Venta venta = new Venta();
        venta.setFecha(dto.getFecha());
        venta.setCliente(cliente);
        venta.setJuego(juego);
        venta.setCantidad(dto.getCantidad());
        venta.setTotal(dto.getTotal());

        log.info("Registrando venta para cliente id: {}", cliente.getId());
        return toResponseDTO(ventaRepository.save(venta));
    }

    public void deleteById(int id) {
        findEntityById(id);
        log.info("Eliminando venta con id: {}", id);
        ventaRepository.deleteById(id);
    }

    private VentaResponseDTO toResponseDTO(Venta venta) {
        return new VentaResponseDTO(
                venta.getId(),
                venta.getFecha(),
                venta.getCliente().getId(),
                venta.getCliente().getNombre(),
                venta.getJuego().getId(),
                venta.getJuego().getTitulo(),
                venta.getCantidad(),
                venta.getTotal());
    }
}
