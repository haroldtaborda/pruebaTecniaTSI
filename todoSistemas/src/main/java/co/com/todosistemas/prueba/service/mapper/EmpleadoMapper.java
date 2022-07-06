package co.com.todosistemas.prueba.service.mapper;

import co.com.todosistemas.prueba.domain.Empleado;
import co.com.todosistemas.prueba.service.dto.EmpleadoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Empleado} and its DTO {@link EmpleadoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmpleadoMapper extends EntityMapper<EmpleadoDTO, Empleado> {}
