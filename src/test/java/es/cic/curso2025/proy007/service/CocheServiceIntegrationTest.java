package es.cic.curso2025.proy007.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.cic.curso2025.proy007.model.Coche;
import es.cic.curso2025.proy007.repository.CocheRepository;

/**
 * Tests de integración para CocheService
 * Verifica la correcta interacción entre el servicio y la capa de persistencia
 * Utiliza una base de datos H2 en memoria
 */
@SpringBootTest
@Transactional
class CocheServiceIntegrationTest {

    @Autowired
    private CocheService cocheService;

    @Autowired
    private CocheRepository cocheRepository;

    /**
     * Limpia la base de datos antes de cada test para asegurar el aislamiento
     */
    @BeforeEach
    void setUp() {
        cocheRepository.deleteAll();
    }

    /**
     * Test de creación de coche
     * Verifica:
     * 1. La generación correcta del ID
     * 2. La persistencia en base de datos
     * 3. La coincidencia de datos guardados
     */
    @Test
    @DisplayName("Debería crear un nuevo coche")
    void testCreate() {
        // Preparamos los datos
        Coche coche = new Coche();
        coche.setMarca("Toyota");
        coche.setPotencia(100);
        coche.setEncendido(false);
        coche.setVersion(1L);

        // Ejecutamos la operación
        Coche created = cocheService.create(coche);

        // Verificamos resultados
        assertNotNull(created.getId(), "El ID no debería ser nulo");
        Optional<Coche> found = cocheRepository.findById(created.getId());
        assertTrue(found.isPresent(), "El coche debería existir en la base de datos");
        assertEquals("Toyota", found.get().getMarca(), "La marca debería coincidir");
    }

    /**
     * Test de búsqueda por ID
     * Verifica:
     * 1. La correcta recuperación de un coche existente
     * 2. La integridad de los datos recuperados
     */
    @Test
    @DisplayName("Debería obtener un coche por ID")
    void testGet() {
        // GIVEN
        Coche coche = new Coche();
        coche.setMarca("Honda");
        coche.setPotencia(120);
        coche.setEncendido(true);
        coche.setVersion(1L);
        coche = cocheRepository.save(coche);

        // WHEN
        Optional<Coche> found = cocheService.get(coche.getId());

        // THEN
        assertTrue(found.isPresent(), "El coche debería existir");
        assertEquals("Honda", found.get().getMarca(), "La marca debería coincidir");
    }

    /**
     * Test de listado completo
     * Verifica:
     * 1. La recuperación de múltiples coches
     * 2. El número correcto de elementos
     * Este test es importante para verificar operaciones bulk
     */
    @Test
    @DisplayName("Debería obtener lista de todos los coches")
    void testGetAll() {
        // GIVEN
        Coche coche1 = new Coche();
        coche1.setMarca("BMW");
        coche1.setPotencia(150);
        coche1.setEncendido(false);
        coche1.setVersion(1L);
        cocheRepository.save(coche1);

        Coche coche2 = new Coche();
        coche2.setMarca("Audi");
        coche2.setPotencia(180);
        coche2.setEncendido(true);
        coche2.setVersion(1L);
        cocheRepository.save(coche2);

        // WHEN
        List<Coche> coches = cocheService.get();

        // THEN
        assertEquals(2, coches.size(), "Deberían haber 2 coches");
    }

    /**
     * Test de actualización
     * Verifica:
     * 1. La persistencia de cambios en un objeto existente
     * 2. La correcta actualización de campos específicos
     * 3. La permanencia de la actualización en base de datos
     */
    @Test
    @DisplayName("Debería actualizar un coche existente")
    void testUpdate() {
        // GIVEN
        Coche coche = new Coche();
        coche.setMarca("Seat");
        coche.setPotencia(90);
        coche.setEncendido(false);
        coche.setVersion(1L);
        coche = cocheRepository.save(coche);

        // WHEN
        coche.setMarca("Volkswagen");
        cocheService.update(coche);

        // THEN
        Optional<Coche> updated = cocheRepository.findById(coche.getId());
        assertTrue(updated.isPresent(), "El coche debería existir");
        assertEquals("Volkswagen", updated.get().getMarca(), "La marca debería estar actualizada");
    }

    /**
     * Test de eliminación
     * Verifica:
     * 1. La correcta eliminación de un registro
     * 2. La no existencia del registro tras el borrado
     * Este test es crítico para evitar registros huérfanos
     */
    @Test
    @DisplayName("Debería eliminar un coche")
    void testDelete() {
        // GIVEN
        Coche coche = new Coche();
        coche.setMarca("Fiat");
        coche.setPotencia(75);
        coche.setEncendido(true);
        coche.setVersion(1L);
        coche = cocheRepository.save(coche);
        Long id = coche.getId();

        // WHEN
        cocheService.delete(id);

        // THEN
        Optional<Coche> deleted = cocheRepository.findById(id);
        assertFalse(deleted.isPresent(), "El coche no debería existir después de eliminarlo");
    }
}
