package co.com.todosistemas.prueba.domain;

import co.com.todosistemas.prueba.domain.enumeration.EstadoActividad;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Actividad.
 */
@Entity
@Table(name = "actividad")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Actividad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "fecha_estimada_ejecucion")
    private Instant fechaEstimadaEjecucion;

    @Column(name = "descripcion")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoActividad estado;

    @Transient
    private String diasRetraso;

    @ManyToOne
    @JsonIgnoreProperties(value = { "actividads" }, allowSetters = true)
    private Empleado empleado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Actividad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Actividad nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Instant getFechaEstimadaEjecucion() {
        return this.fechaEstimadaEjecucion;
    }

    public Actividad fechaEstimadaEjecucion(Instant fechaEstimadaEjecucion) {
        this.setFechaEstimadaEjecucion(fechaEstimadaEjecucion);
        return this;
    }

    public void setFechaEstimadaEjecucion(Instant fechaEstimadaEjecucion) {
        this.fechaEstimadaEjecucion = fechaEstimadaEjecucion;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Actividad descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoActividad getEstado() {
        return this.estado;
    }

    public Actividad estado(EstadoActividad estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoActividad estado) {
        this.estado = estado;
    }

    public Empleado getEmpleado() {
        return this.empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Actividad empleado(Empleado empleado) {
        this.setEmpleado(empleado);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        if (!(o instanceof Actividad)) {
            return false;
        }
        return id != null && id.equals(((Actividad) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Actividad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", fechaEstimadaEjecucion='" + getFechaEstimadaEjecucion() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
