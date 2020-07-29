package modelo;

import excepciones.AccionNoPermitidaException;
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
            throw new ParametrosInvalidosException("No se puede crear una fase sin nombre.");
        registroDeDatos.setNombre(nombre);  }

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

    public void setEstado(EstadoFase nuevoEstado) { estado = nuevoEstado;}


    public void agregarIteracion(Iteracion iteracion) {
        long indiceDeIteracion = iteraciones.size()+1;
        String nombreIteracion = "Iteracion " + indiceDeIteracion;
        iteracion.setNombre(nombreIteracion);
        if (estado.equals(EstadoFase.FINALIZADA))
            throw new AccionNoPermitidaException("La fase se encuentra finalizada");
        setEstado(EstadoFase.ACTIVA);
        this.iteraciones.add(iteracion);
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
                iteraciones.set(i, nuevaIteracion);
                return;
            }
        }
        agregarIteracion(nuevaIteracion);
    }

    public void eliminarIteracion(long idIteracion) {
        if (!estado.equals(EstadoFase.ACTIVA))
            throw new AccionNoPermitidaException("La fase no se encuentra activa");
        Iteracion iteracion = obtenerIteracion(idIteracion);
        if (!iteracion.getEstado().equals(EstadoIteracion.CREADA))
            throw new AccionNoPermitidaException("No se puede eliminar una iteracion activa o finalizada");
        iteraciones.remove(iteracion);
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
}
