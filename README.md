# GotyShop — Evaluación Final Transversal DSY1103

Proyecto de tienda de videojuegos construido con Spring Boot y una arquitectura distribuida. Incluye persistencia JPA/Hibernate con SQLite, migraciones Flyway, validaciones Bean Validation, manejo global de excepciones, logs SLF4J, autenticación JWT, comunicación Feign y un API Gateway.

## Integrantes

- Gabriel Alcalá
- Elliot Bravo
- Gianluca Foradori
- Bastian Henriquez



## Servicios incluidos

| Servicio | Puerto | Responsabilidad |
|---|---:|---|
| `GotyStore_JPA` | 8080 | Clientes, juegos, ventas y validación de token |
| `UsuarioService` | 8081 | Registro, login JWT y consulta de usuarios |
| `ApiGateway` | 8082 | Punto de entrada centralizado y enrutamiento |

La pauta entregada exige al menos 10 microservicios. Este paquete contiene tres servicios; el equipo debe confirmar con el docente si el proyecto semestral completo incluye otros servicios o si debe ampliarse antes de la entrega final.

## Requisitos

- Java 17
- Maven 3.9+
- Docker Desktop (opcional, recomendado)

## Ejecución local

En tres terminales:

```bash
cd UsuarioService
mvn spring-boot:run
```

```bash
cd GotyStore_JPA
mvn spring-boot:run
```

```bash
cd ApiGateway
mvn spring-boot:run
```

El Gateway queda disponible en `http://localhost:8082`.

## Ejecución con Docker

```bash
docker compose up --build
```

Para detener:

```bash
docker compose down
```

## Rutas principales a través del Gateway

- `POST /api/auth/registro`
- `POST /api/auth/login`
- `GET /api/usuarios`
- `GET|POST /api/clientes`
- `GET|POST /api/juegos`
- `GET|POST /api/ventas`

Las rutas protegidas requieren el encabezado:

```http
Authorization: Bearer <token>
```

## Swagger y salud

- GotyStore Swagger: `http://localhost:8080/doc/swagger-ui.html`
- Usuario Swagger: `http://localhost:8081/doc/swagger-ui.html`
- GotyStore health: `http://localhost:8080/actuator/health`
- Usuario health: `http://localhost:8081/actuator/health`
- Gateway health: `http://localhost:8082/actuator/health`

## Pruebas y cobertura

Ejecutar en cada servicio:

```bash
mvn clean test
```

El reporte JaCoCo se genera en:

```text
target/site/jacoco/index.html
```

La existencia de JaCoCo no garantiza automáticamente 80% de cobertura: se debe ejecutar el reporte y revisar el porcentaje real antes de entregar.

## Configuración por variables de entorno

| Variable | Uso |
|---|---|
| `JWT_SECRET` | Secreto compartido para firmar/validar tokens |
| `JWT_EXPIRATION` | Duración del token en milisegundos |
| `USUARIO_SERVICE_URL` | URL usada por GotyStore para llamar a UsuarioService |
| `GOTYSTORE_SERVICE_URL` | URL usada por el Gateway |
| `GOTYSTORE_DB_URL` | URL JDBC de la base GotyStore |
| `USUARIO_DB_URL` | URL JDBC de la base de usuarios |

<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 0d5a096089839342f9d052b8cc0c479dacf4a0f5
## Evidencias que deben agregar antes de entregar

1. Enlace al repositorio GitHub y capturas del historial de commits por integrante.
2. Enlace/capturas del tablero Trello con responsables y estados.
3. Reporte real de cobertura JaCoCo igual o superior a lo solicitado.
4. Colección de Postman con respuestas 200, 201, 400, 401, 404 y 204.
5. Confirmación del número total de microservicios exigido por el docente.
6. Nombre completo del cuarto integrante.

## Advertencia de entrega

Congelar el repositorio en la fecha indicada. La pauta advierte que cambios posteriores a la entrega pueden anular la evaluación.
<<<<<<< HEAD
=======
=======

>>>>>>> 84f9fa07fb994ad29e63b2d8322cfeda8d73abd6
>>>>>>> 0d5a096089839342f9d052b8cc0c479dacf4a0f5
