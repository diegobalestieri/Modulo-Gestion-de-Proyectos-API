package modelo;

import excepciones.AccionNoPermitidaException;

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

    public void setNombre(String nombre) { registroDeDatos.setNombre(nombre);  }

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

    public boolean agregarIteracion(Iteracion iteracion) {
        return this.iteraciones.add(iteracion);
    }

    public List<Iteracion> obtenerIteraciones() { return this.iteraciones;  }

    public Iteracion obtenerIteracion(Long idIteracion) {
        for (Iteracion iteracion : iteraciones) {
            if (iteracion.getId() == idIteracion)
                return iteracion;
        }
        return null;
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
        for (int i = 0; i < iteraciones.size(); ++i){
            Iteracion iteracion = iteraciones.get(i);
            if (iteracion.getId().equals(idIteracion)){
                iteraciones.remove(i);
                return;
            }
        }
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
