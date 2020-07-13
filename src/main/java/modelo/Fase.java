package modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import persistencia.EntidadFase;

import java.text.ParseException;
import java.util.Date;

public class Fase {


    private Long id;
    @JsonBackReference
    private Proyecto proyecto;
    private RegistroDeDatos registroDeDatos = new RegistroDeDatos();

    public Fase() {}
    public Fase(String nombre) {
        registroDeDatos.setNombre(nombre);
    }

    public Fase(EntidadFase entidadFase) {
        this.id = entidadFase.getId();
        this.setNombre(entidadFase.getNombre());
        this.setDescripcion(entidadFase.getDescripcion());
        this.setFechaDeInicio(entidadFase.getFechaDeInicio());
        this.setFechaDeFinalizacion(entidadFase.getFechaDeFinalizacion());
        //this.setProyectoPadre(new Proyecto(entidadFase.getProyectoPadre());
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    private void setFechaDeFinalizacion(Date fechaDeFin) {
        registroDeDatos.setFechaDeFinalizacion(fechaDeFin);
    }

    private void setFechaDeInicio(Date fechaDeInicio) {
        registroDeDatos.setFechaDeInicio(fechaDeInicio);
    }

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

    public EntidadFase obtenerEntidad() {
        EntidadFase entidad = new EntidadFase();
        entidad.setId(id);
        entidad.setNombre(this.getNombre());
        entidad.setDescripcion(this.getDescripcion());
        entidad.setFechaDeFinalizacion(getFechaDeFinalizacion());
        entidad.setFechaDeInicio(getFechaDeInicio());
        //entidad.setProyectoPadre(proyecto.obtenerEntidad());
        return entidad;
    }

    public void setProyectoPadre(Proyecto proyectoPadre) {
        this.proyecto = proyectoPadre;
    }
}
