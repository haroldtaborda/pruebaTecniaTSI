package co.com.todosistemas.prueba.service;

import co.com.todosistemas.prueba.domain.Actividad;
import co.com.todosistemas.prueba.service.dto.ActividadDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link co.com.todosistemas.prueba.domain.Actividad}.
 */
public interface ActividadService {
    /**
     * Save a actividad.
     *
     * @param actividadDTO the entity to save.
     * @return the persisted entity.
     */
    ActividadDTO save(ActividadDTO actividadDTO);

    /**
     * Updates a actividad.
     *
     * @param actividadDTO the entity to update.
     * @return the persisted entity.
     */
    ActividadDTO update(ActividadDTO actividadDTO);

    /**
     * Partially updates a actividad.
     *
     * @param actividadDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ActividadDTO> partialUpdate(ActividadDTO actividadDTO);

    /**
     * Get all the actividads.
     *
     * @return the list of entities.
     */
    List<Actividad> findAll();

    /**
     * Get the "id" actividad.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Actividad> findOne(Long id);

    /**
     * Delete the "id" actividad.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    String diasRetraso(Long id);
}
