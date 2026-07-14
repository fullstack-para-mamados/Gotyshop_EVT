package com.example.GotyStore.controller;

import com.example.GotyStore.assembler.ClienteModelAssembler;
import com.example.GotyStore.dto.ClienteRequestDTO;
import com.example.GotyStore.dto.ClienteResponseDTO;
import com.example.GotyStore.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operaciones relacionadas con los clientes")
public class ClienteController {

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);
    private final ClienteService clienteService;
    private final ClienteModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los clientes", description = "Retorna la lista completa de clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de clientes"),
            @ApiResponse(responseCode = "204", description = "No hay clientes registrados")
    })
    public ResponseEntity<CollectionModel<EntityModel<ClienteResponseDTO>>> getAll() {
        List<EntityModel<ClienteResponseDTO>> clientes = clienteService.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (clientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(clientes,
                linkTo(methodOn(ClienteController.class).getAll()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public EntityModel<ClienteResponseDTO> getById(
            @Parameter(description = "ID del cliente", required = true) @PathVariable int id) {
        return assembler.toModel(clienteService.findById(id));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un cliente nuevo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<ClienteResponseDTO>> create(@Valid @RequestBody ClienteRequestDTO dto) {
        log.info("POST /api/clientes - creando cliente: {}", dto.getCorreo());
        ClienteResponseDTO creado = clienteService.save(dto);
        return ResponseEntity
                .created(linkTo(methodOn(ClienteController.class).getById(creado.getId())).toUri())
                .body(assembler.toModel(creado));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar cliente por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<EntityModel<ClienteResponseDTO>> update(
            @Parameter(description = "ID del cliente", required = true) @PathVariable int id,
            @Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.ok(assembler.toModel(clienteService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del cliente", required = true) @PathVariable int id) {
        clienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
