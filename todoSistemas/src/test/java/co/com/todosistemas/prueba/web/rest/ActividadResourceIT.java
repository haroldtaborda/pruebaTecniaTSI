package co.com.todosistemas.prueba.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.todosistemas.prueba.IntegrationTest;
import co.com.todosistemas.prueba.domain.Actividad;
import co.com.todosistemas.prueba.domain.enumeration.EstadoActividad;
import co.com.todosistemas.prueba.repository.ActividadRepository;
import co.com.todosistemas.prueba.service.dto.ActividadDTO;
import co.com.todosistemas.prueba.service.mapper.ActividadMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ActividadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActividadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_ESTIMADA_EJECUCION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_ESTIMADA_EJECUCION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final EstadoActividad DEFAULT_ESTADO = EstadoActividad.PENDIENTE;
    private static final EstadoActividad UPDATED_ESTADO = EstadoActividad.REALIZADA;

    private static final String ENTITY_API_URL = "/api/actividads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private ActividadMapper actividadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActividadMockMvc;

    private Actividad actividad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actividad createEntity(EntityManager em) {
        Actividad actividad = new Actividad()
            .nombre(DEFAULT_NOMBRE)
            .fechaEstimadaEjecucion(DEFAULT_FECHA_ESTIMADA_EJECUCION)
            .descripcion(DEFAULT_DESCRIPCION)
            .estado(DEFAULT_ESTADO);
        return actividad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actividad createUpdatedEntity(EntityManager em) {
        Actividad actividad = new Actividad()
            .nombre(UPDATED_NOMBRE)
            .fechaEstimadaEjecucion(UPDATED_FECHA_ESTIMADA_EJECUCION)
            .descripcion(UPDATED_DESCRIPCION)
            .estado(UPDATED_ESTADO);
        return actividad;
    }

    @BeforeEach
    public void initTest() {
        actividad = createEntity(em);
    }

    @Test
    @Transactional
    void createActividad() throws Exception {
        int databaseSizeBeforeCreate = actividadRepository.findAll().size();
        // Create the Actividad
        ActividadDTO actividadDTO = actividadMapper.toDto(actividad);
        restActividadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actividadDTO)))
            .andExpect(status().isCreated());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeCreate + 1);
        Actividad testActividad = actividadList.get(actividadList.size() - 1);
        assertThat(testActividad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testActividad.getFechaEstimadaEjecucion()).isEqualTo(DEFAULT_FECHA_ESTIMADA_EJECUCION);
        assertThat(testActividad.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testActividad.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createActividadWithExistingId() throws Exception {
        // Create the Actividad with an existing ID
        actividad.setId(1L);
        ActividadDTO actividadDTO = actividadMapper.toDto(actividad);

        int databaseSizeBeforeCreate = actividadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActividadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actividadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllActividads() throws Exception {
        // Initialize the database
        actividadRepository.saveAndFlush(actividad);

        // Get all the actividadList
        restActividadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actividad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].fechaEstimadaEjecucion").value(hasItem(DEFAULT_FECHA_ESTIMADA_EJECUCION.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    void getActividad() throws Exception {
        // Initialize the database
        actividadRepository.saveAndFlush(actividad);

        // Get the actividad
        restActividadMockMvc
            .perform(get(ENTITY_API_URL_ID, actividad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(actividad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.fechaEstimadaEjecucion").value(DEFAULT_FECHA_ESTIMADA_EJECUCION.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingActividad() throws Exception {
        // Get the actividad
        restActividadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewActividad() throws Exception {
        // Initialize the database
        actividadRepository.saveAndFlush(actividad);

        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();

        // Update the actividad
        Actividad updatedActividad = actividadRepository.findById(actividad.getId()).get();
        // Disconnect from session so that the updates on updatedActividad are not directly saved in db
        em.detach(updatedActividad);
        updatedActividad
            .nombre(UPDATED_NOMBRE)
            .fechaEstimadaEjecucion(UPDATED_FECHA_ESTIMADA_EJECUCION)
            .descripcion(UPDATED_DESCRIPCION)
            .estado(UPDATED_ESTADO);
        ActividadDTO actividadDTO = actividadMapper.toDto(updatedActividad);

        restActividadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, actividadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actividadDTO))
            )
            .andExpect(status().isOk());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
        Actividad testActividad = actividadList.get(actividadList.size() - 1);
        assertThat(testActividad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testActividad.getFechaEstimadaEjecucion()).isEqualTo(UPDATED_FECHA_ESTIMADA_EJECUCION);
        assertThat(testActividad.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testActividad.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingActividad() throws Exception {
        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();
        actividad.setId(count.incrementAndGet());

        // Create the Actividad
        ActividadDTO actividadDTO = actividadMapper.toDto(actividad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActividadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, actividadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actividadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActividad() throws Exception {
        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();
        actividad.setId(count.incrementAndGet());

        // Create the Actividad
        ActividadDTO actividadDTO = actividadMapper.toDto(actividad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActividadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actividadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActividad() throws Exception {
        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();
        actividad.setId(count.incrementAndGet());

        // Create the Actividad
        ActividadDTO actividadDTO = actividadMapper.toDto(actividad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActividadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actividadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActividadWithPatch() throws Exception {
        // Initialize the database
        actividadRepository.saveAndFlush(actividad);

        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();

        // Update the actividad using partial update
        Actividad partialUpdatedActividad = new Actividad();
        partialUpdatedActividad.setId(actividad.getId());

        partialUpdatedActividad.descripcion(UPDATED_DESCRIPCION);

        restActividadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActividad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActividad))
            )
            .andExpect(status().isOk());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
        Actividad testActividad = actividadList.get(actividadList.size() - 1);
        assertThat(testActividad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testActividad.getFechaEstimadaEjecucion()).isEqualTo(DEFAULT_FECHA_ESTIMADA_EJECUCION);
        assertThat(testActividad.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testActividad.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateActividadWithPatch() throws Exception {
        // Initialize the database
        actividadRepository.saveAndFlush(actividad);

        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();

        // Update the actividad using partial update
        Actividad partialUpdatedActividad = new Actividad();
        partialUpdatedActividad.setId(actividad.getId());

        partialUpdatedActividad
            .nombre(UPDATED_NOMBRE)
            .fechaEstimadaEjecucion(UPDATED_FECHA_ESTIMADA_EJECUCION)
            .descripcion(UPDATED_DESCRIPCION)
            .estado(UPDATED_ESTADO);

        restActividadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActividad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActividad))
            )
            .andExpect(status().isOk());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
        Actividad testActividad = actividadList.get(actividadList.size() - 1);
        assertThat(testActividad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testActividad.getFechaEstimadaEjecucion()).isEqualTo(UPDATED_FECHA_ESTIMADA_EJECUCION);
        assertThat(testActividad.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testActividad.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingActividad() throws Exception {
        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();
        actividad.setId(count.incrementAndGet());

        // Create the Actividad
        ActividadDTO actividadDTO = actividadMapper.toDto(actividad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActividadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, actividadDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actividadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActividad() throws Exception {
        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();
        actividad.setId(count.incrementAndGet());

        // Create the Actividad
        ActividadDTO actividadDTO = actividadMapper.toDto(actividad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActividadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actividadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActividad() throws Exception {
        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();
        actividad.setId(count.incrementAndGet());

        // Create the Actividad
        ActividadDTO actividadDTO = actividadMapper.toDto(actividad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActividadMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(actividadDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActividad() throws Exception {
        // Initialize the database
        actividadRepository.saveAndFlush(actividad);

        int databaseSizeBeforeDelete = actividadRepository.findAll().size();

        // Delete the actividad
        restActividadMockMvc
            .perform(delete(ENTITY_API_URL_ID, actividad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
