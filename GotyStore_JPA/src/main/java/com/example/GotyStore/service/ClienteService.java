package com.example.GotyStore.service;

import com.example.GotyStore.Model.Cliente;
import com.example.GotyStore.dto.ClienteRequestDTO;
import com.example.GotyStore.dto.ClienteResponseDTO;
import com.example.GotyStore.exception.RecursoNoEncontradoException;
import com.example.GotyStore.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);
    private final ClienteRepository clienteRepository;

    public List<ClienteResponseDTO> findAll() {
        log.info("Listando todos los clientes");
        return clienteRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO findById(int id) {
        return toResponseDTO(findEntityById(id));
    }

    /**
     * Devuelve la entidad JPA directamente. Uso interno entre servicios
     * (por ejemplo VentaService la necesita para armar la relación @ManyToOne).
     */
    public Cliente findEntityById(int id) {
        log.info("Buscando cliente con id: {}", id);
        return clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cliente con id {} no encontrado", id);
                    return new RecursoNoEncontradoException("Cliente con id " + id + " no encontrado");
                });
    }

    public ClienteResponseDTO save(ClienteRequestDTO dto) {
        if (clienteRepository.existsByCorreo(dto.getCorreo())) {
            log.warn("Intento de duplicar correo: {}", dto.getCorreo());
            throw new IllegalArgumentException("Ya existe un cliente con el correo: " + dto.getCorreo());
        }
        Cliente cliente = fromRequestDTO(dto);
        log.info("Guardando cliente: {}", cliente.getCorreo());
        return toResponseDTO(clienteRepository.save(cliente));
    }

    public ClienteResponseDTO update(int id, ClienteRequestDTO dto) {
        findEntityById(id);
        Cliente cliente = fromRequestDTO(dto);
        cliente.setId(id);
        log.info("Actualizando cliente con id: {}", id);
        return toResponseDTO(clienteRepository.save(cliente));
    }

    public void deleteById(int id) {
        findEntityById(id);
        log.info("Eliminando cliente con id: {}", id);
        clienteRepository.deleteById(id);
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getCorreo(),
                cliente.getDireccion(),
                cliente.getTelefono());
    }

    private Cliente fromRequestDTO(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setCorreo(dto.getCorreo());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        return cliente;
    }
}
