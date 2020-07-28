package modelo;

import excepciones.TareaNotFoundException;

import javax.persistence.*;
import java.text.ParseException;
import java.util.*;

@Entity
@Table(name = "iteraciones")
public class Iteracion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    private RegistroDeDatos registroDeDatos = new RegistroDeDatos();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="ids_tareas",joinColumns = @JoinColumn(name="iteracion_id"))
    private List<Long> idsTareas = new ArrayList<>();

    public Iteracion(String nombre) { registroDeDatos.setNombre(nombre);   }

    public Iteracion(){}

    public List<Long> getIdsTareas() {return idsTareas;} // SOLO PARA JACKSON

    public void setIdsTareas(List<Long> lista) {idsTareas = lista;} // SOLO PARA JACKSON

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void agregarTarea(long idDeTarea) { idsTareas.add(idDeTarea);  }

    //public void setIdsTareas(String nuevasTareas) {idsTareas = nuevasTareas;}

    //public String getIdsTareas() {return this.idsTareas;}

    public List<Long> obtenerTareas() { return new ArrayList<>(this.idsTareas);    }

    public boolean contiene(String idDeTarea) { return idsTareas.contains(idDeTarea); }

    public String getNombre() { return registroDeDatos.getNombre(); }

    public void setNombre(String nombre) { registroDeDatos.setNombre(nombre);  }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Iteracion)) return false;
        Iteracion that = (Iteracion) o;
        return  Objects.equals(registroDeDatos, that.registroDeDatos);
    }

    public List<Tarea> obtenerTareasDeIteracion(List<Tarea> listadoDeTareas) {
        List<Tarea> listaADevolver = new ArrayList();
        for (Tarea tarea : listadoDeTareas) {
            long id_tarea = tarea.getId();
            if (idsTareas.contains(id_tarea))
                listaADevolver.add(tarea);
        }
        return listaADevolver;
    }

    public void eliminarTarea(long idTarea) {
        if (!idsTareas.contains(idTarea)) {
            throw new TareaNotFoundException("La tarea no se encontraba cargada a esta iteracion");
        }
        idsTareas.remove(idTarea);
    }
}
