package es.cic.curso2025.proy007.repository;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import es.cic.curso2025.proy007.model.Coche;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class CocheRepositoryImpl {

    @PersistenceContext
    private EntityManager em;


    private Map<Long, Coche> coches = new HashMap<>();

    public long create(Coche coche) {

        em.persist(coche);

        return coche.getId();

        // long mayor = getSiguienteId();
        // motor.setId(mayor);
        // motores.put(motor.getId(), motor);
        // return motor.getId();
    }

    private long getSiguienteIdConStreams() {
        long mayor = 
            coches
                .keySet()
                .stream()
                .max(
                    (primero, segundo) -> (int) (segundo.longValue() - primero.longValue())
                ).get();
        return mayor + 1;        
    }



    private long getSiguienteId() {
        long mayor = 0;
        
        Set<Long> ids = coches.keySet();
        
        Iterator<Long> iteratorId =  ids.iterator();


        while (iteratorId.hasNext()) {
            Long siguiente = iteratorId.next();
            if (siguiente.longValue() > mayor) {
                mayor = siguiente;
            }
        }
        mayor++;
        return mayor;
    }

}
