package com.example.GotyStore.controller;

import com.example.GotyStore.assembler.JuegoModelAssembler;
import com.example.GotyStore.dto.JuegoRequestDTO;
import com.example.GotyStore.dto.JuegoResponseDTO;
import com.example.GotyStore.service.JuegoService;
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
@RequestMapping("/api/juegos")
@RequiredArgsConstructor
@Tag (name = "Juegos", description = "Operaciones relacionadas con los videojuegos")//
public class JuegoController {

    private final JuegoService juegoService;
    private final JuegoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary =  "Obtener todos los juegos", description = "Retorna la lista completa de juegos" )//
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de juegos"),
        @ApiResponse(responseCode = "204", description = "No hay juegos registrados")
    })
    public ResponseEntity<CollectionModel<EntityModel<JuegoResponseDTO>>> getAll() {
        List<EntityModel<JuegoResponseDTO>> juegos = juegoService.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (juegos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(juegos,
                linkTo(methodOn(JuegoController.class).getAll()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary =  "Obtener un juego por ID") //
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Juego encontrado"),//
        @ApiResponse(responseCode = "404" , description ="Juego no encontrado")//
    })
    public EntityModel<JuegoResponseDTO> getById(@Parameter(description = "ID del juego" , required = true ) @PathVariable int id) {
        return assembler.toModel(juegoService.findById(id));
    }

    @GetMapping(value = "/plataforma/{plataforma}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener juegos por plataforma", description = "Retorna los juegos filtrados por plataforma" )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Juegos encontrados para la plataforma"),
        @ApiResponse(responseCode = "204", description = "No hay juegos para esa plataforma")
    })
    public ResponseEntity<CollectionModel<EntityModel<JuegoResponseDTO>>> getByPlataforma(@Parameter(description = "Nombre de la Plataforma", required = true) @PathVariable String plataforma) {
        List<EntityModel<JuegoResponseDTO>> juegos = juegoService.findByPlataforma(plataforma)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (juegos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(juegos,
                linkTo(methodOn(JuegoController.class).getByPlataforma(plataforma)).withSelfRel()));
    }
    
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un juego nuevo") //
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Juego creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<JuegoResponseDTO>> create(@Valid @RequestBody JuegoRequestDTO dto) {
        JuegoResponseDTO creado = juegoService.save(dto);
        return ResponseEntity
                .created(linkTo(methodOn(JuegoController.class).getById(creado.getId())).toUri())
                .body(assembler.toModel(creado));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar juego por id") //
        @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Juego actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Juego no encontrado")
    })
    public ResponseEntity<EntityModel<JuegoResponseDTO>> update(@Parameter(description = "ID del juego", required = true) @PathVariable int id, @Valid @RequestBody JuegoRequestDTO dto) {
        return ResponseEntity.ok(assembler.toModel(juegoService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar juego por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Juego eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Juego no encontrado")
    })
    public ResponseEntity<Void> delete(@Parameter(description = "ID del juego", required = true) @PathVariable int id) {
        juegoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
