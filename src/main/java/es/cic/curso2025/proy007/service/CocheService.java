package es.cic.curso2025.proy007.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.cic.curso2025.proy007.model.Coche;
import es.cic.curso2025.proy007.repository.CocheRepository;

@Service
public class CocheService {
    @Autowired
    private CocheRepository cocheRepository;


    public long create(Coche coche) {
        // Los días festivos y los fines de semana en esta empresa no se crean motores
        cocheRepository.save(coche);

        return coche.getId();
    }

    public Coche get(long id) {
        Optional<Coche> resultado = cocheRepository.findById(id);
        return resultado.orElse(null);
    }

    public List<Coche> get() {
        return cocheRepository.findAll();
    }

    public void update(Coche coche) {
        cocheRepository.save(coche);
    }

    public void delete(long id) {
        cocheRepository.deleteById(id);
    }
}
