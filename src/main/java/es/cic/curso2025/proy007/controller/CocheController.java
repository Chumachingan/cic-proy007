package es.cic.curso2025.proy007.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;

import es.cic.curso2025.proy007.model.Coche;
import es.cic.curso2025.proy007.service.CocheService;

/**
 * CAPA CONTROLLER (REST)
 *
 * ‑ Expone el recurso /coches => Colección de coches
 * ‑ Convierte HTTP <=> Lógica de negocio (CocheService)
 *
 * NOTA TEMPORAL
 *    
 *    Este controller devuelve directamente Optional<Coche> en
 *    GET /{id}.   Es *correcto* pero **no óptimo**: el cliente recibirá
 *    200 OK + cuerpo null cuando el coche no exista.  Próximos pasos:
 *       1. Cambiar a ResponseEntity (200/404).
 *       2. Finalmente usar una excepción de dominio (CocheException).
 */
@RestController
@RequestMapping("/coches")        
public class CocheController {

    /**
     * INYECCIÓN DE DEPENDENCIAS
     *
     *  Se usa constructor‑injection (sin @Autowired) porque solo hay un constructor. 
     */
    private final CocheService cocheService;

    public CocheController(CocheService cocheService) {
        this.cocheService = cocheService;
    }

    // MÉTODOS CRUD EXPUESTOS COMO ENDPOINTS

    /**
     * GET /coches/{id}
     *
     * IMPORTANTE: al no envolverlo en ResponseEntity, Spring serializa
     * Optional.empty() como `null`, devolviendo 200 OK. 
     */
    @GetMapping("/{id}")
    public Optional<Coche> get(@PathVariable long id) {
        return cocheService.get(id);
    }

    /**
     * GET /coches
     *
     * Lista de todos los coches.
     */
    @GetMapping
    public List<Coche> get() {
        return cocheService.get();
    }

    /**
     * POST /coches
     *
     * Crea un coche.  Devuelve la entidad recibida con el ID rellenado.
     * En una versión REST ideal devolveríamos 201 Created + Location.
     */
    @PostMapping
    public Coche create(@RequestBody Coche coche) {
        cocheService.create(coche);          // El ID se asigna en service
        return coche;                       // El objeto ya contiene getId()
    }

    /**
     * PUT /coches
     *
     * Actualiza un coche existente.  Próxima mejora: usar
     * PUT /coches/{id} para que sea coherente con REST.
     */
    @PutMapping
    public void update(@RequestBody Coche coche) {
        cocheService.update(coche);
    }

    /**
     * DELETE /coches/{id}
     *
     * Elimina el coche indicado.  De momento no devuelve cuerpo.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        cocheService.delete(id);
    }
}
