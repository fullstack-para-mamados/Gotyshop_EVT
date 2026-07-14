package com.example.GotyStore.exception;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void recursoNoEncontradoRetorna404() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleNotFound(new RecursoNoEncontradoException("No encontrado"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().get("status"));
        assertEquals("No encontrado", response.getBody().get("mensaje"));
    }

    @Test
    void argumentoIlegalRetorna400() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleIllegal(new IllegalArgumentException("Dato inválido"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dato inválido", response.getBody().get("mensaje"));
    }

    @Test
    void errorGeneralRetorna500() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleGeneral(new RuntimeException("Falla"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor", response.getBody().get("mensaje"));
    }

    @Test
    void validacionRetornaDetalles() {
        BeanPropertyBindingResult binding =
                new BeanPropertyBindingResult(new Object(), "juego");
        binding.addError(new FieldError("juego", "titulo", "El título no puede estar vacío"));
        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(mock(MethodParameter.class), binding);

        ResponseEntity<Map<String, Object>> response =
                handler.handleValidation(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Datos inválidos", response.getBody().get("error"));
        Map<?, ?> detalles = (Map<?, ?>) response.getBody().get("detalles");
        assertEquals("El título no puede estar vacío", detalles.get("titulo"));
    }

    @Test
    void tipoIncorrectoRetorna400() {
        MethodArgumentTypeMismatchException exception =
                new MethodArgumentTypeMismatchException(
                        "abc", Integer.class, "id", mock(MethodParameter.class), null);

        ResponseEntity<Map<String, Object>> response =
                handler.handleTypeMismatch(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Parámetro inválido: id", response.getBody().get("mensaje"));
    }
}
