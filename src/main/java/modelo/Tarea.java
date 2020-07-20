package modelo;

import com.fasterxml.jackson.annotation.JsonTypeId;
import modelo.Estado.EstadoTarea;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tarea {

    private @JsonTypeId long id;

    private RegistroDeDatos registroDeDatos;
    private String responsable;
    private String prioridad;
    private EstadoTarea estado;

    public Tarea(long id, String nombre) {
        this.id = id;
        this.registroDeDatos = new RegistroDeDatos(nombre);
    }

    public Tarea(){}

    public String getNombre() {
        return registroDeDatos.getNombre();
    }
    public String getDescripcion() { return this.registroDeDatos.getDescripcion();}
    public Date getFechaDeInicio() { return this.registroDeDatos.getFechaDeInicio();}
    public Date getFechaDeFinalizacion() { return this.registroDeDatos.getFechaDeFinalizacion();}

    public void setNombre(String nombre) { this.registroDeDatos.setNombre(nombre);}
    public void setDescripcion(String descripcion) { this.registroDeDatos.setDescripcion(descripcion); }
    public void setFechaDeInicio(String fechaDeInicio) throws ParseException {
        this.registroDeDatos.setFechaDeInicio(fechaDeInicio);
    }
    public void setFechaDeFinalizacion(String fechaDeFinalizacion) throws ParseException {
        this.registroDeDatos.setFechaDeFinalizacion(fechaDeFinalizacion);
    }


    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
}
