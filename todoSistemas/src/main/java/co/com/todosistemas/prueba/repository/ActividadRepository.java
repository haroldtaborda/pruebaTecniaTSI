package co.com.todosistemas.prueba.repository;

import co.com.todosistemas.prueba.domain.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Actividad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {}
