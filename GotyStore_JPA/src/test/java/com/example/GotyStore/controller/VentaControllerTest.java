package com.example.GotyStore.controller;

import com.example.GotyStore.assembler.VentaModelAssembler;
import com.example.GotyStore.dto.VentaRequestDTO;
import com.example.GotyStore.dto.VentaResponseDTO;
import com.example.GotyStore.exception.RecursoNoEncontradoException;
import com.example.GotyStore.security.JwtUtil;
import com.example.GotyStore.service.VentaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VentaController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(VentaModelAssembler.class)
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VentaService ventaService;

    @MockBean
    private JwtUtil jwtUtil;

    private VentaResponseDTO ventaResponse() {
        return new VentaResponseDTO(1, "2026-06-15", 1, "Camila Rojas",
                3, "The Legend of Zelda: Tears of the Kingdom", 2, 39980.0);
    }

    private VentaRequestDTO ventaRequestValida() {
        VentaRequestDTO dto = new VentaRequestDTO();
        dto.setFecha("2026-06-15");
        dto.setIdCliente(1);
        dto.setIdJuego(3);
        dto.setCantidad(2);
        dto.setTotal(39980.0);
        return dto;
    }

    // ---------- GET /api/ventas ----------

    @Test
    @DisplayName("GET /api/ventas - devuelve 200 y los enlaces HATEOAS cuando hay ventas")
    void getAll_conDatos_retorna200ConLinks() throws Exception {
        when(ventaService.findAll()).thenReturn(List.of(ventaResponse()));

        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(content().string(containsString("\"eliminar\"")));
    }

    @Test
    @DisplayName("GET /api/ventas - devuelve 204 (no 200 con lista vacía) cuando no hay ventas")
    void getAll_sinDatos_retorna204SinContenido() throws Exception {
        when(ventaService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("GET /api/ventas/cliente/{clienteId} - devuelve 204 si el cliente no tiene ventas")
    void getByCliente_sinDatos_retorna204() throws Exception {
        when(ventaService.findByClienteId(5)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/ventas/cliente/5"))
                .andExpect(status().isNoContent());
    }

    // ---------- GET /api/ventas/{id} ----------

    @Test
    @DisplayName("GET /api/ventas/{id} - devuelve 200 con el self link cuando la venta existe")
    void getById_existente_retorna200() throws Exception {
        when(ventaService.findById(1)).thenReturn(ventaResponse());

        mockMvc.perform(get("/api/ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(39980.0))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("GET /api/ventas/{id} - devuelve 404 cuando la venta no existe")
    void getById_inexistente_retorna404() throws Exception {
        when(ventaService.findById(99)).thenThrow(new RecursoNoEncontradoException("Venta con id 99 no encontrada"));

        mockMvc.perform(get("/api/ventas/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.not(containsString("_links"))));
    }

    // ---------- POST /api/ventas ----------

    @Test
    @DisplayName("POST /api/ventas - devuelve 201 al registrar una venta válida")
    void create_valida_retorna201() throws Exception {
        when(ventaService.save(any(VentaRequestDTO.class))).thenReturn(ventaResponse());

        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ventaRequestValida())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCliente").value(1));
    }

    @Test
    @DisplayName("POST /api/ventas - devuelve 400 cuando los datos son inválidos")
    void create_invalida_retorna400() throws Exception {
        VentaRequestDTO invalida = new VentaRequestDTO();
        invalida.setFecha("15-06-2026"); // formato incorrecto, viola @Pattern
        invalida.setCantidad(0); // viola @Min(1)

        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalida)))
                .andExpect(status().isBadRequest());
    }

    // ---------- DELETE /api/ventas/{id} ----------

    @Test
    @DisplayName("DELETE /api/ventas/{id} - devuelve 204 al eliminar una venta existente")
    void delete_existente_retorna204() throws Exception {
        mockMvc.perform(delete("/api/ventas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/ventas/{id} - devuelve 404 cuando la venta no existe")
    void delete_inexistente_retorna404() throws Exception {
        org.mockito.Mockito.doThrow(new RecursoNoEncontradoException("Venta con id 99 no encontrada"))
                .when(ventaService).deleteById(99);

        mockMvc.perform(delete("/api/ventas/99"))
                .andExpect(status().isNotFound());
    }
}
