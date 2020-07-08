package modelo;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import excepciones.AccionNoPermitidaException;
import excepciones.RestriccionDeEstadoException;
import modelo.Estado.EstadoProyecto;
import persistencia.EntidadFase;
import persistencia.EntidadProyecto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "tipoDeProyecto")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProyectoDeDesarrollo.class, name = "Desarrollo"),
        @JsonSubTypes.Type(value = ProyectoDeImplementacion.class, name = "Implementación")
})
public abstract class Proyecto {

    protected EstadoProyecto estado = EstadoProyecto.NO_INICIADO;
    protected Long id;
    protected RegistroDeDatos registroDeDatos = new RegistroDeDatos();
    protected String tipoDeProyecto;
    protected List<Fase> fases = new ArrayList<Fase>();

    public Proyecto(){}
    public Proyecto(String nombre){
        this.setNombre(nombre);
    }


    public Proyecto(EntidadProyecto entidadProyecto){
        this.id = entidadProyecto.getId();
        this.setEstado(entidadProyecto.getEstado());
        this.setNombre(entidadProyecto.getNombre());
        this.setDescripcion(entidadProyecto.getDescripcion());
        this.setFechaDeInicio(entidadProyecto.getFechaDeInicio());
        this.setFechaDeFinalizacion(entidadProyecto.getFechaDeFin());
        for(int i = 0; i < entidadProyecto.getFases().size(); ++i){
            this.crearFase(entidadProyecto.getFases().get(i));
        }
    }

    private void crearFase(EntidadFase entidadFase) {
        fases.add(new Fase(entidadFase));
    }


    public void modificar(Proyecto proyecto){
        registroDeDatos.setNombre(proyecto.getNombre());
        this.tipoDeProyecto = proyecto.getTipoDeProyecto();
    }
    public Long getId() {
        return id;
    }

    public String getTipoDeProyecto() { return tipoDeProyecto; }

    public String getNombre() {
        return registroDeDatos.getNombre();
    }
    public String getDescripcion() { return this.registroDeDatos.getDescripcion();}
    public Date getFechaDeInicio() { return this.registroDeDatos.getFechaDeInicio();}
    public Date getFechaDeFinalizacion() { return this.registroDeDatos.getFechaDeFinalizacion();}
    public String getEstado() {
        return estado.getNombre();
    }
    public void setNombre(String nombre) { this.registroDeDatos.setNombre(nombre);}
    public void setDescripcion(String descripcion) { this.registroDeDatos.setDescripcion(descripcion); }
    public void setFechaDeInicio(Date fechaDeInicio){ this.registroDeDatos.setFechaDeInicio(fechaDeInicio);}
    public void setFechaDeInicio(String fechaDeInicio) throws ParseException,RestriccionDeEstadoException {
        if (!estado.getNombre().equals("No iniciado")) {
            throw new RestriccionDeEstadoException("No se puede cambiar la fecha de inicio de un proyecto iniciado");
        }
        registroDeDatos.setFechaDeInicio(fechaDeInicio);
    }
    public void setFechaDeFinalizacion(String fechaDeFinalizacion) throws ParseException {
        this.registroDeDatos.setFechaDeFinalizacion(fechaDeFinalizacion);
    }
    private void setFechaDeFinalizacion(Date fechaDeFin) {
        registroDeDatos.setFechaDeFinalizacion(fechaDeFin);
    }

    public boolean setEstado(String nombreDeEstado) throws AccionNoPermitidaException {
        if (this.estado != EstadoProyecto.NO_INICIADO && nombreDeEstado.equals("No iniciado")) {
            throw new AccionNoPermitidaException("No se puede volver al estado No iniciado");
        }
        switch (nombreDeEstado) {
            case "No iniciado": this.estado = EstadoProyecto.NO_INICIADO;
            break;
            case "Activo": this.estado = EstadoProyecto.ACTIVO;
            break;
            case "Suspendido": this.estado = EstadoProyecto.SUSPENDIDO;
            break;
            case "Cancelado": this.estado = EstadoProyecto.CANCELADO;
            break;
            case "Finalizado": this.estado = EstadoProyecto.FINALIZADO;
            break;
        }
        return true;
    }


    public EntidadProyecto obtenerEntidad() {
        EntidadProyecto entidad = new EntidadProyecto();
        entidad.setId(id);
        entidad.setTipoDeProyecto(tipoDeProyecto);
        entidad.setNombre(registroDeDatos.getNombre());
        entidad.setDescripcion(registroDeDatos.getDescripcion());
        entidad.setEstado(estado.getNombre());
        entidad.setFechaDeInicio(registroDeDatos.getFechaDeInicio());
        entidad.setFechaDeFin(registroDeDatos.getFechaDeFinalizacion());
        entidad.setFases(obtenerEntidadFases());
        ingresarDatos(entidad);
        return entidad;
    }

    protected abstract void ingresarDatos(EntidadProyecto entidad);

    private List<EntidadFase> obtenerEntidadFases() {
        List <EntidadFase> entidadFases = new ArrayList<EntidadFase>();
        for (Fase fase : fases) {
            entidadFases.add(fase.obtenerEntidad());
        }
        return entidadFases;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void actualizar(Map<String, Object> parametros) throws ParseException {
        //No se puede cambiar el tipo de proyecto
        for (Map.Entry<String, Object> entrada : parametros.entrySet()) {
            if (entrada.getKey().equals("nombre")) {
                this.setNombre((String) entrada.getValue());
            } else if (entrada.getKey().equals("descripcion")) {
                this.setDescripcion((String) entrada.getValue());
            } else if (entrada.getKey().equals("fechaDeInicio")) {
                this.setFechaDeInicio((String) entrada.getValue());
            } else if (entrada.getKey().equals("fechaDeFinalizacion")) {
                this.setFechaDeFinalizacion((String) entrada.getValue());
            } else if (entrada.getKey().equals("estado")) {
                this.setEstado((String) entrada.getValue());
            }

        }
    }

    public boolean crearFase(String nombre, String descripcion, String fecha_de_inicio, String fecha_de_finalizacion) {
        Fase aux = new Fase(nombre);
        aux.setDescripcion(descripcion);
        try {
            aux.setFechaDeInicio(fecha_de_inicio);
            aux.setFechaDeFinalizacion(fecha_de_finalizacion);
        } catch (ParseException e) {
            return false;
        }
        fases.add(aux);
        return true;
    }

    public List<Fase> obtenerFases() {
        return fases;
    }
}
