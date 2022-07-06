package co.com.todosistemas.prueba.web.rest;

import co.com.todosistemas.prueba.domain.Actividad;
import co.com.todosistemas.prueba.repository.ActividadRepository;
import co.com.todosistemas.prueba.service.ActividadService;
import co.com.todosistemas.prueba.service.dto.ActividadDTO;
import co.com.todosistemas.prueba.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.com.todosistemas.prueba.domain.Actividad}.
 */
@RestController
@RequestMapping("/api")
public class ActividadResource {

    private final Logger log = LoggerFactory.getLogger(ActividadResource.class);

    private static final String ENTITY_NAME = "actividad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActividadService actividadService;

    private final ActividadRepository actividadRepository;

    public ActividadResource(ActividadService actividadService, ActividadRepository actividadRepository) {
        this.actividadService = actividadService;
        this.actividadRepository = actividadRepository;
    }

    /**
     * {@code POST  /actividads} : Create a new actividad.
     *
     * @param actividadDTO the actividadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new actividadDTO, or with status {@code 400 (Bad Request)} if the actividad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/actividads")
    public ResponseEntity<ActividadDTO> createActividad(@RequestBody ActividadDTO actividadDTO) throws URISyntaxException {
        log.debug("REST request to save Actividad : {}", actividadDTO);
        if (actividadDTO.getId() != null) {
            throw new BadRequestAlertException("A new actividad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActividadDTO result = actividadService.save(actividadDTO);
        return ResponseEntity
            .created(new URI("/api/actividads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /actividads/:id} : Updates an existing actividad.
     *
     * @param id the id of the actividadDTO to save.
     * @param actividadDTO the actividadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actividadDTO,
     * or with status {@code 400 (Bad Request)} if the actividadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the actividadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/actividads/{id}")
    public ResponseEntity<ActividadDTO> updateActividad(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActividadDTO actividadDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Actividad : {}, {}", id, actividadDTO);
        if (actividadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, actividadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!actividadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ActividadDTO result = actividadService.update(actividadDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, actividadDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /actividads/:id} : Partial updates given fields of an existing actividad, field will ignore if it is null
     *
     * @param id the id of the actividadDTO to save.
     * @param actividadDTO the actividadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actividadDTO,
     * or with status {@code 400 (Bad Request)} if the actividadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the actividadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the actividadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/actividads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ActividadDTO> partialUpdateActividad(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActividadDTO actividadDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Actividad partially : {}, {}", id, actividadDTO);
        if (actividadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, actividadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!actividadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ActividadDTO> result = actividadService.partialUpdate(actividadDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, actividadDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /actividads} : get all the actividads.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of actividads in body.
     */
    @GetMapping("/actividads")
    public List<Actividad> getAllActividads() {
        log.debug("REST request to get all Actividads");
        return actividadService.findAll();
    }

    /**
     * {@code GET  /actividads/:id} : get the "id" actividad.
     *
     * @param id the id of the actividadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the actividadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/actividads/{id}")
    public ResponseEntity<Actividad> getActividad(@PathVariable Long id) {
        log.debug("REST request to get Actividad : {}", id);
        Optional<Actividad> actividadDTO = actividadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(actividadDTO);
    }

    @GetMapping("/activadsFindFelayDays/{id}")
    public ResponseEntity<String> getValueDelayDays(@PathVariable Long id) {
        log.debug("REST request to get value of delay days");
        String resp = actividadService.diasRetraso(id);
        return new ResponseEntity<String>(resp, HttpStatus.OK);
    }

    /**
     * {@code DELETE  /actividads/:id} : delete the "id" actividad.
     *
     * @param id the id of the actividadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/actividads/{id}")
    public ResponseEntity<Void> deleteActividad(@PathVariable Long id) {
        log.debug("REST request to delete Actividad : {}", id);
        actividadService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
