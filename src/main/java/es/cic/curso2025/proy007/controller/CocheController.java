package es.cic.curso2025.proy007.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import es.cic.curso2025.proy007.model.Coche;
import es.cic.curso2025.proy007.service.CocheService;

@RestController
@RequestMapping("/coches")
public class CocheController {

    private final CocheService cocheService;

    public CocheController(CocheService cocheService) {
        this.cocheService = cocheService;
    }

    @GetMapping("/{id}")
    public Optional<Coche> get(@PathVariable long id) {
        Optional<Coche> coche = cocheService.get(id);
        return coche;
    }

    @GetMapping
    public List<Coche> get() {
        return cocheService.get();
    }

    @PostMapping
    public Coche create(@RequestBody Coche coche) {
        cocheService.create(coche);
        return coche;
    }

    @PutMapping
    public void update(@RequestBody Coche coche) {
        cocheService.update(coche);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        cocheService.delete(id);
    }
}
