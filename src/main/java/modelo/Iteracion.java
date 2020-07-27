package modelo;

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

    private String idsTareas = "";

    public Iteracion(String nombre) { registroDeDatos.setNombre(nombre);   }

    public Iteracion(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void agregarTarea(String idDeTarea) {
        if (idsTareas.equals("")) { idsTareas += idDeTarea;}
        else { idsTareas += ","+idDeTarea; }
    }

    public void setIdsTareas(String nuevasTareas) {idsTareas = nuevasTareas;}

    public String getIdsTareas() {return this.idsTareas;}

    public List<String> obtenerTareas() {
        return Arrays.asList(idsTareas.split(","));
    }

    public boolean contiene(String idDeTarea) { return obtenerTareas().contains(idDeTarea); }

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
            String id_tarea = tarea.getId().toString();
            if (obtenerTareas().contains(id_tarea))
                listaADevolver.add(tarea);
        }
        return listaADevolver;
    }
}
