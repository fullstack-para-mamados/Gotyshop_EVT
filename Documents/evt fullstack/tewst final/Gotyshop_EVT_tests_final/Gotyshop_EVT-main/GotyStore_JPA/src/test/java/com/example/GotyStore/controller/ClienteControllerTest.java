package com.example.GotyStore.controller;

import com.example.GotyStore.assembler.ClienteModelAssembler;
import com.example.GotyStore.dto.ClienteRequestDTO;
import com.example.GotyStore.dto.ClienteResponseDTO;
import com.example.GotyStore.security.JwtUtil;
import com.example.GotyStore.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ClienteModelAssembler.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private JwtUtil jwtUtil;

    private ClienteResponseDTO response() {
        return new ClienteResponseDTO(1, "Camila Rojas",
                "camila@correo.com", "Santiago", "+56912345678");
    }

    private ClienteRequestDTO request() {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNombre("Camila Rojas");
        dto.setCorreo("camila@correo.com");
        dto.setDireccion("Santiago");
        dto.setTelefono("+56912345678");
        return dto;
    }

    @Test
    void getAllConDatosRetorna200() throws Exception {
        when(clienteService.findAll()).thenReturn(List.of(response()));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void getAllSinDatosRetorna204() throws Exception {
        when(clienteService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getByIdRetorna200() throws Exception {
        when(clienteService.findById(1)).thenReturn(response());

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("camila@correo.com"));
    }

    @Test
    void createRetorna201() throws Exception {
        when(clienteService.save(any(ClienteRequestDTO.class))).thenReturn(response());

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Camila Rojas"));
    }

    @Test
    void createInvalidoRetorna400() throws Exception {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNombre("");
        dto.setCorreo("correo-invalido");

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateRetorna200() throws Exception {
        when(clienteService.update(anyInt(), any(ClienteRequestDTO.class))).thenReturn(response());

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteRetorna204() throws Exception {
        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());

        verify(clienteService).deleteById(1);
    }
}
