package com.example.GotyStore.assembler;

import com.example.GotyStore.controller.ClienteController;
import com.example.GotyStore.controller.JuegoController;
import com.example.GotyStore.controller.VentaController;
import com.example.GotyStore.dto.VentaResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VentaModelAssembler
        implements RepresentationModelAssembler<VentaResponseDTO, EntityModel<VentaResponseDTO>> {

    @Override
    public EntityModel<VentaResponseDTO> toModel(VentaResponseDTO venta) {
        return EntityModel.of(venta,
                linkTo(methodOn(VentaController.class).getById(venta.getId())).withSelfRel(),
                linkTo(methodOn(VentaController.class).getAll()).withRel("ventas"),
                linkTo(methodOn(ClienteController.class).getById(venta.getIdCliente())).withRel("cliente"),
                linkTo(methodOn(JuegoController.class).getById(venta.getIdJuego())).withRel("juego"),
                linkTo(methodOn(VentaController.class).delete(venta.getId())).withRel("eliminar"));
    }
}
