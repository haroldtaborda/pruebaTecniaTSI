package co.com.todosistemas.prueba.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActividadMapperTest {

    private ActividadMapper actividadMapper;

    @BeforeEach
    public void setUp() {
        actividadMapper = new ActividadMapperImpl();
    }
}
