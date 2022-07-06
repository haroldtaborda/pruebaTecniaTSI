package co.com.todosistemas.prueba.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.todosistemas.prueba.IntegrationTest;
import co.com.todosistemas.prueba.domain.Empleado;
import co.com.todosistemas.prueba.domain.enumeration.TipoIdentificacion;
import co.com.todosistemas.prueba.repository.EmpleadoRepository;
import co.com.todosistemas.prueba.service.dto.EmpleadoDTO;
import co.com.todosistemas.prueba.service.mapper.EmpleadoMapper;
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
 * Integration tests for the {@link EmpleadoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmpleadoResourceIT {

    private static final String DEFAULT_NOMBRES = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRES = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final String DEFAULT_FECHA_NACIMIENTO = "AAAAAAAAAA";
    private static final String UPDATED_FECHA_NACIMIENTO = "BBBBBBBBBB";

    private static final TipoIdentificacion DEFAULT_TIPO_IDENTIFICACION = TipoIdentificacion.CC;
    private static final TipoIdentificacion UPDATED_TIPO_IDENTIFICACION = TipoIdentificacion.CE;

    private static final String DEFAULT_NUMERO_IDENTIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_IDENTIFICACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/empleados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private EmpleadoMapper empleadoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmpleadoMockMvc;

    private Empleado empleado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empleado createEntity(EntityManager em) {
        Empleado empleado = new Empleado()
            .nombres(DEFAULT_NOMBRES)
            .apellidos(DEFAULT_APELLIDOS)
            .tipoIdentificacion(DEFAULT_TIPO_IDENTIFICACION)
            .numeroIdentificacion(DEFAULT_NUMERO_IDENTIFICACION);
        return empleado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empleado createUpdatedEntity(EntityManager em) {
        Empleado empleado = new Empleado()
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .tipoIdentificacion(UPDATED_TIPO_IDENTIFICACION)
            .numeroIdentificacion(UPDATED_NUMERO_IDENTIFICACION);
        return empleado;
    }

    @BeforeEach
    public void initTest() {
        empleado = createEntity(em);
    }

    @Test
    @Transactional
    void createEmpleado() throws Exception {
        int databaseSizeBeforeCreate = empleadoRepository.findAll().size();
        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);
        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empleadoDTO)))
            .andExpect(status().isCreated());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeCreate + 1);
        Empleado testEmpleado = empleadoList.get(empleadoList.size() - 1);
        assertThat(testEmpleado.getNombres()).isEqualTo(DEFAULT_NOMBRES);
        assertThat(testEmpleado.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testEmpleado.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testEmpleado.getTipoIdentificacion()).isEqualTo(DEFAULT_TIPO_IDENTIFICACION);
        assertThat(testEmpleado.getNumeroIdentificacion()).isEqualTo(DEFAULT_NUMERO_IDENTIFICACION);
    }

    @Test
    @Transactional
    void createEmpleadoWithExistingId() throws Exception {
        // Create the Empleado with an existing ID
        empleado.setId(1L);
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        int databaseSizeBeforeCreate = empleadoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmpleados() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empleado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO)))
            .andExpect(jsonPath("$.[*].tipoIdentificacion").value(hasItem(DEFAULT_TIPO_IDENTIFICACION.toString())))
            .andExpect(jsonPath("$.[*].numeroIdentificacion").value(hasItem(DEFAULT_NUMERO_IDENTIFICACION)));
    }

    @Test
    @Transactional
    void getEmpleado() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);

        // Get the empleado
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL_ID, empleado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(empleado.getId().intValue()))
            .andExpect(jsonPath("$.nombres").value(DEFAULT_NOMBRES))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO))
            .andExpect(jsonPath("$.tipoIdentificacion").value(DEFAULT_TIPO_IDENTIFICACION.toString()))
            .andExpect(jsonPath("$.numeroIdentificacion").value(DEFAULT_NUMERO_IDENTIFICACION));
    }

    @Test
    @Transactional
    void getNonExistingEmpleado() throws Exception {
        // Get the empleado
        restEmpleadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmpleado() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);

        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();

        // Update the empleado
        Empleado updatedEmpleado = empleadoRepository.findById(empleado.getId()).get();
        // Disconnect from session so that the updates on updatedEmpleado are not directly saved in db
        em.detach(updatedEmpleado);
        updatedEmpleado
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .tipoIdentificacion(UPDATED_TIPO_IDENTIFICACION)
            .numeroIdentificacion(UPDATED_NUMERO_IDENTIFICACION);
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(updatedEmpleado);

        restEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empleadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(empleadoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
        Empleado testEmpleado = empleadoList.get(empleadoList.size() - 1);
        assertThat(testEmpleado.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testEmpleado.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testEmpleado.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testEmpleado.getTipoIdentificacion()).isEqualTo(UPDATED_TIPO_IDENTIFICACION);
        assertThat(testEmpleado.getNumeroIdentificacion()).isEqualTo(UPDATED_NUMERO_IDENTIFICACION);
    }

    @Test
    @Transactional
    void putNonExistingEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();
        empleado.setId(count.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empleadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();
        empleado.setId(count.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();
        empleado.setId(count.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empleadoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmpleadoWithPatch() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);

        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();

        // Update the empleado using partial update
        Empleado partialUpdatedEmpleado = new Empleado();
        partialUpdatedEmpleado.setId(empleado.getId());

        partialUpdatedEmpleado.nombres(UPDATED_NOMBRES).apellidos(UPDATED_APELLIDOS).tipoIdentificacion(UPDATED_TIPO_IDENTIFICACION);

        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpleado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmpleado))
            )
            .andExpect(status().isOk());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
        Empleado testEmpleado = empleadoList.get(empleadoList.size() - 1);
        assertThat(testEmpleado.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testEmpleado.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testEmpleado.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testEmpleado.getTipoIdentificacion()).isEqualTo(UPDATED_TIPO_IDENTIFICACION);
        assertThat(testEmpleado.getNumeroIdentificacion()).isEqualTo(DEFAULT_NUMERO_IDENTIFICACION);
    }

    @Test
    @Transactional
    void fullUpdateEmpleadoWithPatch() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);

        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();

        // Update the empleado using partial update
        Empleado partialUpdatedEmpleado = new Empleado();
        partialUpdatedEmpleado.setId(empleado.getId());

        partialUpdatedEmpleado
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .tipoIdentificacion(UPDATED_TIPO_IDENTIFICACION)
            .numeroIdentificacion(UPDATED_NUMERO_IDENTIFICACION);

        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpleado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmpleado))
            )
            .andExpect(status().isOk());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
        Empleado testEmpleado = empleadoList.get(empleadoList.size() - 1);
        assertThat(testEmpleado.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testEmpleado.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testEmpleado.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testEmpleado.getTipoIdentificacion()).isEqualTo(UPDATED_TIPO_IDENTIFICACION);
        assertThat(testEmpleado.getNumeroIdentificacion()).isEqualTo(UPDATED_NUMERO_IDENTIFICACION);
    }

    @Test
    @Transactional
    void patchNonExistingEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();
        empleado.setId(count.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, empleadoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();
        empleado.setId(count.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();
        empleado.setId(count.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(empleadoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmpleado() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);

        int databaseSizeBeforeDelete = empleadoRepository.findAll().size();

        // Delete the empleado
        restEmpleadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, empleado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
