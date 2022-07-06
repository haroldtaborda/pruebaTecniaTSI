package co.com.todosistemas.prueba.domain;

import co.com.todosistemas.prueba.domain.enumeration.TipoIdentificacion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Empleado.
 */
@Entity
@Table(name = "empleado")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "fecha_nacimiento")
    private Instant fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_identificacion")
    private TipoIdentificacion tipoIdentificacion;

    @Column(name = "numero_identificacion")
    private String numeroIdentificacion;

    @OneToMany(mappedBy = "empleado")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "empleado" }, allowSetters = true)
    private Set<Actividad> actividads = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Empleado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return this.nombres;
    }

    public Empleado nombres(String nombres) {
        this.setNombres(nombres);
        return this;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public Empleado apellidos(String apellidos) {
        this.setApellidos(apellidos);
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Empleado fechaNacimiento(Instant fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public TipoIdentificacion getTipoIdentificacion() {
        return this.tipoIdentificacion;
    }

    public Empleado tipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        this.setTipoIdentificacion(tipoIdentificacion);
        return this;
    }

    public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNumeroIdentificacion() {
        return this.numeroIdentificacion;
    }

    public Empleado numeroIdentificacion(String numeroIdentificacion) {
        this.setNumeroIdentificacion(numeroIdentificacion);
        return this;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public Set<Actividad> getActividads() {
        return this.actividads;
    }

    public Instant getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Instant fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setActividads(Set<Actividad> actividads) {
        if (this.actividads != null) {
            this.actividads.forEach(i -> i.setEmpleado(null));
        }
        if (actividads != null) {
            actividads.forEach(i -> i.setEmpleado(this));
        }
        this.actividads = actividads;
    }

    public Empleado actividads(Set<Actividad> actividads) {
        this.setActividads(actividads);
        return this;
    }

    public Empleado addActividad(Actividad actividad) {
        this.actividads.add(actividad);
        actividad.setEmpleado(this);
        return this;
    }

    public Empleado removeActividad(Actividad actividad) {
        this.actividads.remove(actividad);
        actividad.setEmpleado(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empleado)) {
            return false;
        }
        return id != null && id.equals(((Empleado) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Empleado{" +
            "id=" + getId() +
            ", nombres='" + getNombres() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", tipoIdentificacion='" + getTipoIdentificacion() + "'" +
            ", numeroIdentificacion='" + getNumeroIdentificacion() + "'" +
            "}";
    }
}
