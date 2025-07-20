package es.cic.curso2025.proy007.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.cic.curso2025.proy007.model.Coche;
import es.cic.curso2025.proy007.repository.CocheRepository;

/**
 * Prueba de integración del REST Controller.
 * Levanta el contexto completo de Spring Boot.
 * Usa BD embebida (H2) y MockMvc para invocar endpoints reales.
 * Con @Transactional los cambios se revierten al terminar cada test.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CocheControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CocheRepository cocheRepository;

    // Garantiza un estado limpio antes de cada método.
    @BeforeEach
    void clearDb() {
        cocheRepository.deleteAll();
    }

    /**
     * 
     * POST /coches
     */
    @Test
    @DisplayName("POST /coches guarda el coche y devuelve JSON con id")
    void shouldCreateCoche() throws Exception {

        /**
         * PREPARACIÓN
         * Creamos el objeto y no asignamos id, ya que lo hace JPA
         */
        Coche coche = new Coche();
        coche.setMarca("Audi");
        coche.setPotencia(90);
        coche.setEncendido(false);
        coche.setVersion(1L);

        // Serializamos a JSON
        String json = objectMapper.writeValueAsString(coche);

        /**
         * EJECUCIÓN
         * Llamamos al endpoint real
         */
        MvcResult res = mockMvc.perform(post("/coches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        /**
         * COMPROBACIÓN
         * Des-serializamos la respuesta convirtiéndola a objeto
         */
        Coche body = objectMapper.readValue(
                res.getResponse().getContentAsString(),
                Coche.class);

        // Confirmamos que que el id > 0
        assertTrue(body.getId() > 0, "El id debe ser positivo");

        /**
         * Verificamos que realmente se persistió, es decir
         * consultamos la base de datos despues de la operación
         * y aseguramos que el registro está allí realmente
         */
        Optional<Coche> enBd = cocheRepository.findById(body.getId());
        assertTrue(enBd.isPresent(), "El coche no se encontró en la base de datos");
    }

    /**
     * GET /coches/{id}
     */
    @Test
    @DisplayName("GET /coches/{id} devuelve objeto cuando existe y 'null' cuando no")
    void shouldReturnCocheOrNull() throws Exception {

        // Creamos un objeto coche que almacenaremos en la base de datos
        Coche coche = new Coche();
        coche.setMarca("BMW");
        coche.setPotencia(120);
        coche.setEncendido(true);
        coche.setVersion(1L);

        coche = cocheRepository.save(coche);

        // Caso 1: el coche existe
        mockMvc.perform(get("/coches/{id}", coche.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(coche.getId()))
                .andExpect(jsonPath("$.marca").value("BMW"));

        // Caso 2: el coche NO existe
        mockMvc.perform(get("/coches/{id}", coche.getId() + 999))
                .andExpect(status().isOk()) // 200 + cuerpo 'null'
                .andExpect(content().string("null"));
    }

    /**
     * DELETE /coches/{id}
     */
    @Test
    @DisplayName("DELETE /coches/{id} elimina el registro")
    void shouldDeleteCoche() throws Exception {

        // Creamos un objeto coche que almacenaremos en la base de datos
        Coche coche = new Coche();
        coche.setMarca("Seat");
        coche.setPotencia(75);
        coche.setEncendido(false);
        coche.setVersion(1L);

        coche = cocheRepository.save(coche);

        // Ejecutamos la petición DELETE
        mockMvc.perform(delete("/coches/{id}", coche.getId()))
                .andExpect(status().isOk());

        // Confirmamos que ya no existe en la base de datos
        Optional<Coche> enBd = cocheRepository.findById(coche.getId());
        assertTrue(enBd.isEmpty(), "El coche debería haber sido eliminado");
    }
}
