package persistencia;

import com.fasterxml.jackson.annotation.JsonBackReference;
import modelo.Proyecto;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "fases")
public class EntidadFase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "proyecto_Id")
    private EntidadProyecto proyecto;
    private String nombre;
    private String descripcion;
    private String estado;
    private Date fechaDeInicio;
    private Date fechaDeFin;

    public EntidadFase(){}

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaDeInicio() {
        return fechaDeInicio;
    }

    public void setFechaDeInicio(Date fechaDeInicio) {
        this.fechaDeInicio = fechaDeInicio;
    }

    public Date getFechaDeFinalizacion() {
        return fechaDeFin;
    }

    public void setFechaDeFinalizacion(Date fechaDeFin) {
        this.fechaDeFin = fechaDeFin;
    }

    public void setProyectoPadre(EntidadProyecto proyectoPadre) {
        this.proyecto = proyectoPadre;
    }
    public EntidadProyecto getProyectoPadre() {
        return this.proyecto;
    }
}
