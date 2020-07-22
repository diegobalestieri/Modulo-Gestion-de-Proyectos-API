package modelo;

import com.fasterxml.jackson.annotation.JsonTypeId;
import excepciones.AccionNoPermitidaException;
import excepciones.FechaInvalidaException;
import modelo.Estado.EstadoProyecto;
import modelo.Estado.EstadoTarea;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "tareas")
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;
    private String descripcion;
    private Date fechaDeInicio;
    private Date fechaDeFinalizacion;
    private String prioridad;
    private EstadoTarea estado;

    public Tarea () {}

    public Tarea(String nombre) {
        this.nombre = nombre;
        this.estado = EstadoTarea.TO_DO;
    }

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

    public Date getFechaDeInicio() {
        return fechaDeInicio;
    }

    public void setFechaDeInicio(Date fechaDeInicio) {
        this.fechaDeInicio = fechaDeInicio;
    }

    public Date getFechaDeFinalizacion() {
        return fechaDeFinalizacion;
    }

    public void setFechaDeFinalizacion(Date fechaDeFin) {
        this.fechaDeFinalizacion = fechaDeFin;
    }

    public void setFechaDeInicio(String fecha_de_inicio) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.fechaDeInicio = format.parse(fecha_de_inicio);
    }

    public void setFechaDeFinalizacion(String fecha_de_fin) throws ParseException,FechaInvalidaException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date nuevaFechadeFinalizacion = format.parse(fecha_de_fin);
        if (this.fechaDeInicio != null && nuevaFechadeFinalizacion.compareTo(this.fechaDeInicio) < 0) {
            throw new FechaInvalidaException("La fecha de finalizacion debe ser posterior a la de inicio");
        }
        this.fechaDeFinalizacion = nuevaFechadeFinalizacion;
    }

    public String getDescripcion() { return this.descripcion;}

    public void setDescripcion(String descripcion) { this.descripcion = descripcion;}

    public String getPrioridad() { return this.prioridad; }

    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public EstadoTarea getEstado() {
        return this.estado;
    }

    public void setEstado(String nombreDeEstado) {
        switch (nombreDeEstado) {
            case "Por hacer": this.estado = EstadoTarea.TO_DO;
                break;
            case "En progreso": this.estado = EstadoTarea.IN_PROGRESS;
                break;
            case "Bloqueada": this.estado = EstadoTarea.BLOCKED;
                break;
            case "Finalizada": this.estado = EstadoTarea.DONE;
                break;
        }
    }
    public void setEstado(EstadoTarea nuevoEstado) { this.estado = nuevoEstado; }

}
