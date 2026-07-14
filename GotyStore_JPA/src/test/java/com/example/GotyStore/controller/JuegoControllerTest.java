package com.example.GotyStore.controller;

import com.example.GotyStore.assembler.JuegoModelAssembler;
import com.example.GotyStore.dto.JuegoRequestDTO;
import com.example.GotyStore.dto.JuegoResponseDTO;
import com.example.GotyStore.exception.RecursoNoEncontradoException;
import com.example.GotyStore.security.JwtUtil;
import com.example.GotyStore.service.JuegoService;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(JuegoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(JuegoModelAssembler.class)
class JuegoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JuegoService juegoService;

    @MockBean
    private JwtUtil jwtUtil;

    private JuegoResponseDTO juegoResponse() {
        return new JuegoResponseDTO(1, "The Legend of Zelda: Tears of the Kingdom",
                "Nintendo EPD", "Nintendo Switch", "2023", "Aventura");
    }

    private JuegoRequestDTO juegoRequestValido() {
        JuegoRequestDTO dto = new JuegoRequestDTO();
        dto.setTitulo("The Legend of Zelda: Tears of the Kingdom");
        dto.setDesarrollador("Nintendo EPD");
        dto.setPlataforma("Nintendo Switch");
        dto.setAnioLanzamiento("2023");
        dto.setGenero("Aventura");
        return dto;
    }


    @Test
    @DisplayName("GET /api/juegos - devuelve 200 y los enlaces HATEOAS cuando hay juegos")
    void getAll_conDatos_retorna200ConLinks() throws Exception {
        when(juegoService.findAll()).thenReturn(List.of(juegoResponse()));

        mockMvc.perform(get("/api/juegos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(content().string(containsString("\"actualizar\"")))
                .andExpect(content().string(containsString("\"eliminar\"")));
    }

    @Test
    @DisplayName("GET /api/juegos - devuelve 204 (no 200 con lista vacía) cuando no hay juegos")
    void getAll_sinDatos_retorna204SinContenido() throws Exception {
        when(juegoService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/juegos"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    // ---------- GET /api/juegos/plataforma/{plataforma} ----------

    @Test
    @DisplayName("GET /api/juegos/plataforma/{plataforma} - devuelve 204 cuando no hay juegos para esa plataforma")
    void getByPlataforma_sinDatos_retorna204() throws Exception {
        when(juegoService.findByPlataforma(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/juegos/plataforma/PS5"))
                .andExpect(status().isNoContent());
    }

    // ---------- GET /api/juegos/{id} ----------

    @Test
    @DisplayName("GET /api/juegos/{id} - devuelve 200 con el self link cuando el juego existe")
    void getById_existente_retorna200() throws Exception {
        when(juegoService.findById(1)).thenReturn(juegoResponse());

        mockMvc.perform(get("/api/juegos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("The Legend of Zelda: Tears of the Kingdom"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("GET /api/juegos/{id} - devuelve 404 cuando el juego no existe")
    void getById_inexistente_retorna404() throws Exception {
        when(juegoService.findById(99)).thenThrow(new RecursoNoEncontradoException("Juego con id 99 no encontrado"));

        mockMvc.perform(get("/api/juegos/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.not(containsString("_links"))));
    }

    // ---------- POST /api/juegos ----------

    @Test
    @DisplayName("POST /api/juegos - devuelve 201 al crear un juego válido")
    void create_valido_retorna201() throws Exception {
        when(juegoService.save(any(JuegoRequestDTO.class))).thenReturn(juegoResponse());

        mockMvc.perform(post("/api/juegos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(juegoRequestValido())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.plataforma").value("Nintendo Switch"));
    }

    @Test
    @DisplayName("POST /api/juegos - devuelve 400 cuando los datos son inválidos")
    void create_invalido_retorna400() throws Exception {
        JuegoRequestDTO invalido = new JuegoRequestDTO();
        invalido.setTitulo(""); // viola @NotBlank
        invalido.setAnioLanzamiento("no-es-un-año"); // viola @Pattern

        mockMvc.perform(post("/api/juegos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    // ---------- PUT /api/juegos/{id} ----------

    @Test
    @DisplayName("PUT /api/juegos/{id} - devuelve 200 al actualizar un juego existente")
    void update_existente_retorna200() throws Exception {
        when(juegoService.update(anyInt(), any(JuegoRequestDTO.class))).thenReturn(juegoResponse());

        mockMvc.perform(put("/api/juegos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(juegoRequestValido())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    // ---------- DELETE /api/juegos/{id} ----------

    @Test
    @DisplayName("DELETE /api/juegos/{id} - devuelve 204 al eliminar un juego existente")
    void delete_existente_retorna204() throws Exception {
        mockMvc.perform(delete("/api/juegos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/juegos/{id} - devuelve 404 cuando el juego no existe")
    void delete_inexistente_retorna404() throws Exception {
        org.mockito.Mockito.doThrow(new RecursoNoEncontradoException("Juego con id 99 no encontrado"))
                .when(juegoService).deleteById(99);

        mockMvc.perform(delete("/api/juegos/99"))
                .andExpect(status().isNotFound());
    }
}
