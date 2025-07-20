package es.cic.curso2025.proy007.controller;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired CocheRepository cocheRepository;

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

        Coche coche = new Coche();
        coche.setMarca("Audi");
        coche.setPotencia(90);
        coche.setEncendido(false);
        coche.setVersion(1L);                // El campo 'id' lo genera JPA

        String json = objectMapper.writeValueAsString(coche);

        MvcResult res = mockMvc.perform(post("/coches")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isOk())     
                                .andReturn();

        // Convertimos la respuesta a objeto
        Coche body = objectMapper.readValue(
                        res.getResponse().getContentAsString(),
                        Coche.class);

        assertThat(body.getId()).isPositive();

        // Verificamos que realmente se persistió
        Optional<Coche> enBd = cocheRepository.findById(body.getId());
        assertThat(enBd).isPresent();
    }

    /**
     * GET /coches/{id}
     */
    @Test
    @DisplayName("GET /coches/{id} devuelve objeto cuando existe y 'null' cuando no")
    void shouldReturnCocheOrNull() throws Exception {

        // ─ 1) Persistimos un coche
        Coche c = new Coche();
        c.setMarca("BMW");
        c.setPotencia(120);
        c.setEncendido(true);
        c.setVersion(1L);

        c = cocheRepository.save(c);

        // ─ 2) Caso: existe
        mockMvc.perform(get("/coches/{id}", c.getId()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(c.getId()))
               .andExpect(jsonPath("$.marca").value("BMW"));

        // ─ 3) Caso: no existe
        mockMvc.perform(get("/coches/{id}", c.getId() + 999))
               .andExpect(status().isOk())          // 200 + cuerpo 'null'
               .andExpect(content().string("null"));
    }

    /**
     * DELETE /coches/{id}
     */
    @Test
    @DisplayName("DELETE /coches/{id} elimina el registro")
    void shouldDeleteCoche() throws Exception {

        Coche c = new Coche();
        c.setMarca("Seat");
        c.setPotencia(75);
        c.setEncendido(false);
        c.setVersion(1L);

        c = cocheRepository.save(c);

        mockMvc.perform(delete("/coches/{id}", c.getId()))
               .andExpect(status().isOk());

        assertThat(cocheRepository.findById(c.getId())).isNotPresent();
    }
}
