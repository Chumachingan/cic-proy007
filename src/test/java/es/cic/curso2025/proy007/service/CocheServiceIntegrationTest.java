package es.cic.curso2025.proy007.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.cic.curso2025.proy007.exception.CocheException;
import es.cic.curso2025.proy007.model.Coche;
import es.cic.curso2025.proy007.repository.CocheRepository;

/**
 * Pruebas de integración para CocheService.
 * Verifica la correcta interacción entre el servicio y la base de datos.
 * Usa una base de datos H2 en memoria y transacciones que se revierten después de cada test.
 */
@SpringBootTest
@Transactional
class CocheServiceIntegrationTest {

    @Autowired
    private CocheService cocheService;

    @Autowired
    private CocheRepository cocheRepository;

    /**
     * Limpia la base de datos antes de cada prueba para asegurar un estado inicial conocido.
     */
    @BeforeEach
    void setUp() {
        cocheRepository.deleteAll();
    }

    /**
     * Verifica la creación de un coche nuevo:
     * 1. Asigna un ID automáticamente
     * 2. Persiste correctamente en base de datos
     * 3. Retorna el objeto completo
     */
    @Test
    @DisplayName("create() debe persistir un nuevo coche y retornarlo con ID")
    void shouldCreateCoche() {
        // Preparación
        Coche coche = new Coche();
        coche.setMarca("Tesla");
        coche.setPotencia(500);
        coche.setEncendido(false);
        coche.setVersion(1L);

        // Ejecución
        Coche resultado = cocheService.create(coche);

        // Verificación
        assertNotNull(resultado.getId(), "El ID no debería ser null");
        assertTrue(resultado.getId() > 0, "El ID debería ser positivo");
        
        // Verificar que está en la base de datos
        assertTrue(cocheRepository.findById(resultado.getId()).isPresent(), 
            "El coche debería existir en la base de datos");
    }

    /**
     * Verifica que se puede recuperar un coche existente por su ID
     * y que todos sus atributos coinciden con los almacenados.
     */
    @Test
    @DisplayName("get() debe retornar el coche si existe")
    void shouldGetExistingCoche() {
        // Preparación
        Coche coche = new Coche();
        coche.setMarca("Ford");
        coche.setPotencia(150);
        coche.setEncendido(true);
        coche.setVersion(1L);
        coche = cocheRepository.save(coche);

        // Ejecución
        Coche resultado = cocheService.get(coche.getId());

        // Verificación
        assertNotNull(resultado, "El coche no debería ser null");
        assertEquals("Ford", resultado.getMarca(), "La marca debería coincidir");
        assertEquals(150, resultado.getPotencia(), "La potencia debería coincidir");
        assertTrue(resultado.isEncendido(), "El estado debería coincidir");
    }

    /**
     * Verifica que se lanza la excepción apropiada cuando
     * se intenta recuperar un coche que no existe.
     */
    @Test
    @DisplayName("get() debe lanzar excepción si el coche no existe")
    void shouldThrowExceptionWhenCocheNotFound() {
        // Verificación
        assertThrows(CocheException.class, () -> {
            cocheService.get(999L);
        }, "Debería lanzar CocheException cuando el ID no existe");
    }

    /**
     * Verifica que se pueden recuperar todos los coches
     * y que la lista contiene el número correcto de elementos.
     */
    @Test
    @DisplayName("get() sin argumentos debe retornar todos los coches")
    void shouldGetAllCoches() {
        // Preparación
        Coche coche1 = new Coche();
        coche1.setMarca("BMW");
        coche1.setPotencia(200);
        coche1.setEncendido(false);
        coche1.setVersion(1L);
        cocheRepository.save(coche1);

        Coche coche2 = new Coche();
        coche2.setMarca("Mercedes");
        coche2.setPotencia(250);
        coche2.setEncendido(true);
        coche2.setVersion(1L);
        cocheRepository.save(coche2);

        // Ejecución
        List<Coche> resultado = cocheService.get();

        // Verificación
        assertEquals(2, resultado.size(), "Deberían haber 2 coches");
    }

    /**
     * Verifica que se puede eliminar un coche existente
     * y que realmente desaparece de la base de datos.
     */
    @Test
    @DisplayName("delete() debe eliminar el coche existente")
    void shouldDeleteCoche() {
        // Preparación
        Coche coche = new Coche();
        coche.setMarca("Opel");
        coche.setPotencia(100);
        coche.setEncendido(false);
        coche.setVersion(1L);
        coche = cocheRepository.save(coche);
        Long id = coche.getId();

        // Ejecución
        cocheService.delete(id);

        // Verificación
        assertFalse(cocheRepository.findById(id).isPresent(), 
            "El coche no debería existir después de eliminarlo");
    }

    /**
     * Verifica que se puede actualizar un coche existente
     * y que los cambios se reflejan en la base de datos.
     */
    @Test
    @DisplayName("update() debe modificar un coche existente")
    void shouldUpdateCoche() {
        // Preparación
        Coche coche = new Coche();
        coche.setMarca("Seat");
        coche.setPotencia(120);
        coche.setEncendido(false);
        coche.setVersion(1L);
        coche = cocheRepository.save(coche);

        // Modificamos el coche
        coche.setMarca("Volkswagen");
        coche.setPotencia(150);

        // Ejecución
        cocheService.update(coche);

        // Verificación
        Coche actualizado = cocheRepository.findById(coche.getId()).orElseThrow();
        assertEquals("Volkswagen", actualizado.getMarca(), "La marca debería estar actualizada");
        assertEquals(150, actualizado.getPotencia(), "La potencia debería estar actualizada");
    }
}
