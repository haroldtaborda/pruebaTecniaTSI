package co.com.todosistemas.prueba.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.todosistemas.prueba.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActividadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActividadDTO.class);
        ActividadDTO actividadDTO1 = new ActividadDTO();
        actividadDTO1.setId(1L);
        ActividadDTO actividadDTO2 = new ActividadDTO();
        assertThat(actividadDTO1).isNotEqualTo(actividadDTO2);
        actividadDTO2.setId(actividadDTO1.getId());
        assertThat(actividadDTO1).isEqualTo(actividadDTO2);
        actividadDTO2.setId(2L);
        assertThat(actividadDTO1).isNotEqualTo(actividadDTO2);
        actividadDTO1.setId(null);
        assertThat(actividadDTO1).isNotEqualTo(actividadDTO2);
    }
}
