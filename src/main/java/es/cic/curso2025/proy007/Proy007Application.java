package es.cic.curso2025.proy007;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Proy007Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Proy007Application.class);

    public static void main(String[] args) {
        LOGGER.info("Iniciando aplicación");
        SpringApplication.run(Proy007Application.class, args);
    }
}

