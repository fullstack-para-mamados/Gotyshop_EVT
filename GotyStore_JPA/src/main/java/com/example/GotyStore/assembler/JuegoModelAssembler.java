package com.example.GotyStore.assembler;

import com.example.GotyStore.controller.JuegoController;
import com.example.GotyStore.controller.VentaController;
import com.example.GotyStore.dto.JuegoResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class JuegoModelAssembler
        implements RepresentationModelAssembler<JuegoResponseDTO, EntityModel<JuegoResponseDTO>> {

    @Override
    public EntityModel<JuegoResponseDTO> toModel(JuegoResponseDTO juego) {
        return EntityModel.of(juego,
                linkTo(methodOn(JuegoController.class).getById(juego.getId())).withSelfRel(),
                linkTo(methodOn(JuegoController.class).getAll()).withRel("juegos"),
                linkTo(methodOn(VentaController.class).getByJuego(juego.getId())).withRel("ventas"),
                linkTo(methodOn(JuegoController.class).update(juego.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(JuegoController.class).delete(juego.getId())).withRel("eliminar"));
    }
}
