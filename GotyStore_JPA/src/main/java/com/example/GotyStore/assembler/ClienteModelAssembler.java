package com.example.GotyStore.assembler;

import com.example.GotyStore.controller.ClienteController;
import com.example.GotyStore.controller.VentaController;
import com.example.GotyStore.dto.ClienteResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClienteModelAssembler
        implements RepresentationModelAssembler<ClienteResponseDTO, EntityModel<ClienteResponseDTO>> {

    @Override
    public EntityModel<ClienteResponseDTO> toModel(ClienteResponseDTO cliente) {
        return EntityModel.of(cliente,
                linkTo(methodOn(ClienteController.class).getById(cliente.getId())).withSelfRel(),
                linkTo(methodOn(ClienteController.class).getAll()).withRel("clientes"),
                linkTo(methodOn(VentaController.class).getByCliente(cliente.getId())).withRel("ventas"),
                linkTo(methodOn(ClienteController.class).update(cliente.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(ClienteController.class).delete(cliente.getId())).withRel("eliminar"));
    }
}
