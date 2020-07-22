package modelo;

import com.fasterxml.jackson.annotation.JsonTypeId;
import excepciones.AccionNoPermitidaException;
import excepciones.FechaInvalidaException;
import modelo.Estado.EstadoProyecto;
import modelo.Estado.EstadoTarea;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tareas")
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Embedded
    private RegistroDeDatos registroDeDatos = new RegistroDeDatos();
    private String responsable;
    private String prioridad;
    private EstadoTarea estado = EstadoTarea.TO_DO;

    public Tarea(){}
    public Tarea(String nombre) {
        setNombre(nombre);
    }


    public String getNombre() {
        return registroDeDatos.getNombre();
    }

    public void setNombre(String nombre) { this.registroDeDatos.setNombre(nombre);}
    public void setDescripcion(String descripcion) { this.registroDeDatos.setDescripcion(descripcion); }
    public void setFechaDeInicio(Date fechaDeInicio){
        registroDeDatos.setFechaDeInicio(fechaDeInicio);
    }
    public void setFechaDeFinalizacion(Date fechaDeFinalizacion){
        registroDeDatos.setFechaDeFinalizacion(fechaDeFinalizacion);
    }
    public String getPrioridad() {
        return prioridad;
    }
    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public void setFechaDeFinalizacion(String fechaDeFinalizacion) throws ParseException {
        this.registroDeDatos.setFechaDeFinalizacion(fechaDeFinalizacion);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tarea)) return false;
        Tarea tarea = (Tarea) o;
        return  Objects.equals(registroDeDatos, tarea.registroDeDatos) &&
                Objects.equals(responsable, tarea.responsable) &&
                Objects.equals(prioridad, tarea.prioridad) &&
                estado == tarea.estado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, registroDeDatos, responsable, prioridad, estado);
    }
    public void setEstado(String nombreDeEstado) {
        switch (nombreDeEstado) {
            case "Por hacer": this.estado = EstadoTarea.TO_DO;
            case "En progreso": this.estado = EstadoTarea.IN_PROGRESS;
            case "Bloqueada": this.estado = EstadoTarea.BLOCKED;
            case "Finalizada": this.estado = EstadoTarea.DONE;
        }
    }

    public String getDescripcion() {
        return registroDeDatos.getDescripcion();
    }

    public Date getFechaDeInicio() { return registroDeDatos.getFechaDeInicio();
    }

    public Date getFechaDeFinalizacion() {return registroDeDatos.getFechaDeInicio();
    }
}
