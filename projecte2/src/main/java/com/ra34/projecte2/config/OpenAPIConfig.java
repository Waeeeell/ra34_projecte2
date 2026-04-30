package com.ra34.projecte2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI/Swagger para documentación de la API REST.
 * Disponible en: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🛒 API REST - Tienda Online de Productos")
                        .description("API REST para gestionar productos de una tienda online.\n\n" +
                                "Implementa CRUD completo, búsquedas avanzadas, carga de CSV transaccional y paginación.\n\n" +
                                "**Tecnologías:** Spring Boot, Spring Data JPA, H2/MySQL, Hibernate ORM")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo DAM")
                                .url("https://github.com/AD_PROJECTE_2")
                                .email("info@tiendaonline.cat"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
