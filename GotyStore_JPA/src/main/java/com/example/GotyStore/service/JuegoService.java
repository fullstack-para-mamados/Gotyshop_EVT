package com.example.GotyStore.service;

import com.example.GotyStore.Model.Juego;
import com.example.GotyStore.dto.JuegoRequestDTO;
import com.example.GotyStore.dto.JuegoResponseDTO;
import com.example.GotyStore.exception.RecursoNoEncontradoException;
import com.example.GotyStore.repository.JuegoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JuegoService {

    private static final Logger log = LoggerFactory.getLogger(JuegoService.class);
    private final JuegoRepository juegoRepository;

    public List<JuegoResponseDTO> findAll() {
        log.info("Listando todos los juegos");
        return juegoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public JuegoResponseDTO findById(int id) {
        return toResponseDTO(findEntityById(id));
    }

    /**
     * Devuelve la entidad JPA directamente. Uso interno entre servicios
     * (por ejemplo VentaService la necesita para armar la relación @ManyToOne).
     */
    public Juego findEntityById(int id) {
        log.info("Buscando juego con id: {}", id);
        return juegoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Juego con id {} no encontrado", id);
                    return new RecursoNoEncontradoException("Juego con id " + id + " no encontrado");
                });
    }

    public List<JuegoResponseDTO> findByPlataforma(String plataforma) {
        log.info("Listando juegos por plataforma: {}", plataforma);
        return juegoRepository.findByPlataforma(plataforma)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public JuegoResponseDTO save(JuegoRequestDTO dto) {
        Juego juego = fromRequestDTO(dto);
        log.info("Guardando juego: {}", juego.getTitulo());
        return toResponseDTO(juegoRepository.save(juego));
    }

    public JuegoResponseDTO update(int id, JuegoRequestDTO dto) {
        findEntityById(id);
        Juego juego = fromRequestDTO(dto);
        juego.setId(id);
        log.info("Actualizando juego con id: {}", id);
        return toResponseDTO(juegoRepository.save(juego));
    }

    public void deleteById(int id) {
        findEntityById(id);
        log.info("Eliminando juego con id: {}", id);
        juegoRepository.deleteById(id);
    }

    private JuegoResponseDTO toResponseDTO(Juego juego) {
        return new JuegoResponseDTO(
                juego.getId(),
                juego.getTitulo(),
                juego.getDesarrollador(),
                juego.getPlataforma(),
                juego.getAnioLanzamiento(),
                juego.getGenero());
    }

    private Juego fromRequestDTO(JuegoRequestDTO dto) {
        Juego juego = new Juego();
        juego.setTitulo(dto.getTitulo());
        juego.setDesarrollador(dto.getDesarrollador());
        juego.setPlataforma(dto.getPlataforma());
        juego.setAnioLanzamiento(dto.getAnioLanzamiento());
        juego.setGenero(dto.getGenero());
        return juego;
    }
}
