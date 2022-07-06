package co.com.todosistemas.prueba.service.dto;

import co.com.todosistemas.prueba.domain.enumeration.EstadoActividad;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link co.com.todosistemas.prueba.domain.Actividad} entity.
 */
public class ActividadDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private String nombre;

    private Instant fechaEstimadaEjecucion;

    private String descripcion;

    private EstadoActividad estado;

    private EmpleadoDTO empleado;

    private String diasRetraso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Instant getFechaEstimadaEjecucion() {
        return fechaEstimadaEjecucion;
    }

    public void setFechaEstimadaEjecucion(Instant fechaEstimadaEjecucion) {
        this.fechaEstimadaEjecucion = fechaEstimadaEjecucion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoActividad getEstado() {
        return estado;
    }

    public void setEstado(EstadoActividad estado) {
        this.estado = estado;
    }

    public EmpleadoDTO getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoDTO empleado) {
        this.empleado = empleado;
    }

    public String getDiasRetraso() {
        return diasRetraso;
    }

    public void setDiasRetraso(String diasRetraso) {
        this.diasRetraso = diasRetraso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActividadDTO)) {
            return false;
        }

        ActividadDTO actividadDTO = (ActividadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, actividadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActividadDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", fechaEstimadaEjecucion='" + getFechaEstimadaEjecucion() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", estado='" + getEstado() + "'" +
            ", empleado=" + getEmpleado() +
            "}";
    }
}
