package com.easypark.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
public class PlatformApplication {

    public static void main(String[] args) {
        // Configurar zona horaria de Perú para toda la aplicación
        TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
        SpringApplication.run(PlatformApplication.class, args);
    }
}
