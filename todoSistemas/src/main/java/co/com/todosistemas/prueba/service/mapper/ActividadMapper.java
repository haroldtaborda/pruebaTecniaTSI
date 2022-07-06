package co.com.todosistemas.prueba.service.mapper;

import co.com.todosistemas.prueba.domain.Actividad;
import co.com.todosistemas.prueba.domain.Empleado;
import co.com.todosistemas.prueba.service.dto.ActividadDTO;
import co.com.todosistemas.prueba.service.dto.EmpleadoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Actividad} and its DTO {@link ActividadDTO}.
 */
@Mapper(componentModel = "spring")
public interface ActividadMapper extends EntityMapper<ActividadDTO, Actividad> {
    @Mapping(target = "empleado", source = "empleado", qualifiedByName = "empleadoId")
    ActividadDTO toDto(Actividad s);

    @Named("empleadoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpleadoDTO toDtoEmpleadoId(Empleado empleado);
}
