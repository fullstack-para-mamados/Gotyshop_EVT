package com.example.GotyStore.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
        
  

    @Bean
    public OpenAPI customOpenAPI() {
        final String schemeName= "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("API GotyStore")
                        .version("1.0")
                        .description("Documentación de la API para la tienda de videojuegos GotyStore. " +
                                "Incluye gestión de juegos, clientes, ventas y autenticación.")
                        .contact(new Contact()
                                .name("Equipo GotyShop")
                                .email("contacto@gotyshop.com"))
                        .license(new License()
                                .name("Uso académico - Duoc UC")
                                .url("https://www.duoc.cl")))
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .components(new Components()
                        .addSecuritySchemes(schemeName, new SecurityScheme()
                                .name(schemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}