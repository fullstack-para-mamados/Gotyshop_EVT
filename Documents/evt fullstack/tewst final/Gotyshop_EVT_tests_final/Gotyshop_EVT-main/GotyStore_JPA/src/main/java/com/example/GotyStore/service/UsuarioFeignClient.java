package com.example.GotyStore.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "usuario-service", url = "${usuario.service.url}")
public interface UsuarioFeignClient {

    @GetMapping("/api/usuarios/existe")
    Boolean existeUsuario(@RequestParam("email") String email);

    @GetMapping("/api/usuarios/buscar")
    Map<String, Object> buscarUsuario(@RequestParam("email") String email);
}
