package es.cic.curso2025.proy007.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import es.cic.curso2025.proy007.model.Coche;


public interface CocheRepository extends JpaRepository<Coche, Long> {

}
