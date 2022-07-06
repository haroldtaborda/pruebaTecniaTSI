package co.com.todosistemas.prueba.service.dto;

import co.com.todosistemas.prueba.domain.enumeration.TipoIdentificacion;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link co.com.todosistemas.prueba.domain.Empleado} entity.
 */
public class EmpleadoDTO implements Serializable {

    private Long id;

    private String nombres;

    private String apellidos;

    private Instant fechaNacimiento;

    private TipoIdentificacion tipoIdentificacion;

    private String numeroIdentificacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Instant getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Instant fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public TipoIdentificacion getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmpleadoDTO)) {
            return false;
        }

        EmpleadoDTO empleadoDTO = (EmpleadoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, empleadoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpleadoDTO{" +
            "id=" + getId() +
            ", nombres='" + getNombres() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", tipoIdentificacion='" + getTipoIdentificacion() + "'" +
            ", numeroIdentificacion='" + getNumeroIdentificacion() + "'" +
            "}";
    }
}
