package modelo;

import excepciones.AccionNoPermitidaException;
import excepciones.FechaInvalidaException;
import excepciones.IteracionNotFoundException;
import excepciones.ParametrosInvalidosException;
import modelo.Estado.EstadoFase;
import modelo.Estado.EstadoIteracion;

import javax.persistence.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "fases")
public class Fase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fase_id")
    private Long id;
    @Embedded
    private RegistroDeDatos registroDeDatos = new RegistroDeDatos();

    private EstadoFase estado = EstadoFase.CREADA;
    private long cantidadDeIteraciones = 0;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Iteracion> iteraciones = new ArrayList<>();

    public Fase(){}

    //public int hashCode() {  return Objects.hash(id, registroDeDatos, iteraciones); }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() { return registroDeDatos.getNombre(); }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.equals(""))
            throw new ParametrosInvalidosException("No se puede crear una fase sin nombre");
        registroDeDatos.setNombre(nombre);
    }

    public String getDescripcion() { return registroDeDatos.getDescripcion(); }

    public void setDescripcion(String descripcion) { registroDeDatos.setDescripcion(descripcion); }

    public Date getFechaDeInicio() {
        return registroDeDatos.getFechaDeInicio();
    }

    public void setFechaDeInicio(Date fechaDeInicio) { registroDeDatos.setFechaDeInicio(fechaDeInicio); }

    public Date getFechaDeFinalizacion() {
        return registroDeDatos.getFechaDeFinalizacion();
    }

    public void setFechaDeFinalizacion(Date fechaDeFin) { registroDeDatos.setFechaDeFinalizacion(fechaDeFin); }

    public void asignarFechaDeInicio(String fecha_de_inicio) throws ParseException {
        registroDeDatos.asignarFechaDeInicio(fecha_de_inicio);
    }
    public void asignarFechaDeFinalizacion(String fecha_de_finalizacion) throws ParseException {
        registroDeDatos.asignarFechaDeFinalizacion(fecha_de_finalizacion);
    }

    public EstadoFase getEstado() { return estado; }

    public void setIteraciones(List<Iteracion> nuevaLista) {iteraciones = nuevaLista;}

    public void setEstado(EstadoFase nuevoEstado) { estado = nuevoEstado;}

    public long getCantidadDeIteraciones() { return cantidadDeIteraciones;}

    public void setCantidadDeIteraciones(long nuevaCantidad) { cantidadDeIteraciones = nuevaCantidad; }

    public void agregarIteracion(Iteracion iteracion) {
        if (estado.equals(EstadoFase.FINALIZADA))
            throw new AccionNoPermitidaException("La fase se encuentra finalizada");
        validarFechasDeIteracion(iteracion);
        long indiceDeIteracion = cantidadDeIteraciones+1;
        String nombreIteracion = "Iteracion " + indiceDeIteracion;
        iteracion.setNombre(nombreIteracion);
        if (indiceDeIteracion == 1)
            iteracion.setEstado(EstadoIteracion.ACTIVA);
        setEstado(EstadoFase.ACTIVA);
        this.iteraciones.add(iteracion);
        //actualizarIteracionActiva();
        cantidadDeIteraciones++;
    }

    public List<Iteracion> obtenerIteraciones() { return this.iteraciones;  }

    public Iteracion obtenerIteracion(Long idIteracion) {
        for (Iteracion iteracion : iteraciones) {
            if (iteracion.getId().equals(idIteracion))
                return iteracion;
        }
        throw new IteracionNotFoundException("No se pudo encontrar la iteracion");
    }

    public void guardarIteracion(Iteracion nuevaIteracion) {
        for (int i = 0; i < iteraciones.size(); ++i){
            Iteracion iteracion = iteraciones.get(i);
            if (iteracion.getId().equals(nuevaIteracion.getId())){
                validarFechasDeIteracion(nuevaIteracion);
                nuevaIteracion.setIdsTareas(iteracion.getIdsTareas());
                iteraciones.set(i, nuevaIteracion);

                return;
            }
        }
        agregarIteracion(nuevaIteracion);
    }

    public void eliminarIteracion(long idIteracion) {
        if (estado.equals(EstadoFase.FINALIZADA))
            throw new AccionNoPermitidaException("La fase se encuentra finalizada, no se puede eliminar la iteración");
        Iteracion iteracion = obtenerIteracion(idIteracion);
        if (!iteracion.getIdsTareas().isEmpty())
            throw new AccionNoPermitidaException("No se puede eliminar una iteracion que cuenta con tareas cargadas");
        iteraciones.remove(iteracion);
        actualizarIteracionActiva();
    }
    /*
    public void borrarIteracion(long iteracionId) throws AccionNoPermitidaException {
        if (!iteraciones.contains(iteracionId)) {return;}
        Iteracion iteracion = obtenerIteracion(iteracionId);
        if (!iteracion.obtenerTareas().isEmpty()) {
            throw new AccionNoPermitidaException("No se puede eliminar una iteracion con tareas");
        }
        eliminarIteracion(iteracionId);
    }
    */

    public void validarFechasDeIteracion(Iteracion iteracion) {
        if (registroDeDatos.getFechaDeInicio() == null && registroDeDatos.getFechaDeFinalizacion() == null)
            return;
        Date fechaDeInicioDeIteracion = iteracion.getFechaDeInicio();
        Date fechaDeInicioDeFase = registroDeDatos.getFechaDeInicio();
        if (fechaDeInicioDeIteracion != null && fechaDeInicioDeFase != null) {
            if (fechaDeInicioDeIteracion.compareTo(fechaDeInicioDeFase) < 0)
                throw new FechaInvalidaException("La fecha de inicio de una iteración no puede ser anterior a la de la fase que la contiene");
        }
        Date fechaDeFinalizacionDeIteracion = iteracion.getFechaDeFinalizacion();
        Date fechaDeFinalizacionDeFase = registroDeDatos.getFechaDeFinalizacion();
        if (fechaDeFinalizacionDeIteracion != null && fechaDeFinalizacionDeFase != null) {
            if (fechaDeFinalizacionDeIteracion.compareTo(fechaDeFinalizacionDeFase) > 0)
                throw new FechaInvalidaException("La fecha de finalización de una iteración no puede ser posterior a la de la fase que la contiene");
        }
    }
    public void actualizarIteracionActiva(){
        for (Iteracion iteracionActual : iteraciones){
            if (iteracionActual.getEstado() != EstadoIteracion.FINALIZADA){
                iteracionActual.setEstado(EstadoIteracion.ACTIVA);
                return;
            }
        }
    }

    public boolean contieneLaIteracion(long iteracionId) {
        for (Iteracion iteracion : iteraciones) {
            if (iteracion.getId().equals(iteracionId))
                return true;
        }
        return false;
    }
}
