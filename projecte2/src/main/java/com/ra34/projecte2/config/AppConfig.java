package com.ra34.projecte2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuración general de la aplicación.
 * Habilita manejo de transacciones automático.
 */
@Configuration
@EnableTransactionManagement
public class AppConfig {
    // Las configuraciones de JPA se encuentran en application.properties
}
