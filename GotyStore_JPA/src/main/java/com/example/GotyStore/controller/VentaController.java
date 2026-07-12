package com.example.GotyStore.controller;

import com.example.GotyStore.assembler.VentaModelAssembler;
import com.example.GotyStore.dto.VentaRequestDTO;
import com.example.GotyStore.dto.VentaResponseDTO;
import com.example.GotyStore.service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Tag(name = "Ventas", description = "Operaciones relacionadas con las ventas")
public class VentaController {

    private final VentaService ventaService;
    private final VentaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todas las ventas", description = "Retorna la lista completa de ventas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de ventas"),
            @ApiResponse(responseCode = "204", description = "No hay ventas registradas")
    })
    public ResponseEntity<CollectionModel<EntityModel<VentaResponseDTO>>> getAll() {
        List<EntityModel<VentaResponseDTO>> ventas = ventaService.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (ventas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(ventas,
                linkTo(methodOn(VentaController.class).getAll()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener una venta por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta encontrada"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    public EntityModel<VentaResponseDTO> getById(
            @Parameter(description = "ID de la venta", required = true) @PathVariable int id) {
        return assembler.toModel(ventaService.findById(id));
    }

    @GetMapping(value = "/cliente/{clienteId}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener ventas por cliente", description = "Retorna las ventas asociadas a un cliente específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ventas encontradas para el cliente"),
            @ApiResponse(responseCode = "204", description = "El cliente no tiene ventas registradas")
    })
    public ResponseEntity<CollectionModel<EntityModel<VentaResponseDTO>>> getByCliente(
            @Parameter(description = "ID del cliente", required = true) @PathVariable int clienteId) {
        List<EntityModel<VentaResponseDTO>> ventas = ventaService.findByClienteId(clienteId)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (ventas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(ventas,
                linkTo(methodOn(VentaController.class).getByCliente(clienteId)).withSelfRel()));
    }

    @GetMapping(value = "/juego/{juegoId}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener ventas por juego", description = "Retorna las ventas asociadas a un juego específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ventas encontradas para el juego"),
            @ApiResponse(responseCode = "204", description = "El juego no tiene ventas registradas")
    })
    public ResponseEntity<CollectionModel<EntityModel<VentaResponseDTO>>> getByJuego(
            @Parameter(description = "ID del juego", required = true) @PathVariable int juegoId) {
        List<EntityModel<VentaResponseDTO>> ventas = ventaService.findByJuegoId(juegoId)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (ventas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(ventas,
                linkTo(methodOn(VentaController.class).getByJuego(juegoId)).withSelfRel()));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Registrar una venta nueva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venta registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<VentaResponseDTO>> create(@Valid @RequestBody VentaRequestDTO dto) {
        VentaResponseDTO creada = ventaService.save(dto);
        return ResponseEntity
                .created(linkTo(methodOn(VentaController.class).getById(creada.getId())).toUri())
                .body(assembler.toModel(creada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar venta por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Venta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la venta", required = true) @PathVariable int id) {
        ventaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
