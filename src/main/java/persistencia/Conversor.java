package persistencia;

import modelo.Proyecto;
import modelo.ProyectoDeDesarrollo;
import modelo.ProyectoDeImplementacion;

import java.util.ArrayList;
import java.util.List;

public class Conversor {

    public Conversor() {
    }
    public Proyecto obtenerProyecto(EntidadProyecto proyecto){
        if (proyecto.getTipoDeProyecto().equals("Desarrollo")){
            return new ProyectoDeDesarrollo(proyecto);
        }else{
            return new ProyectoDeImplementacion(proyecto);
        }
    }

    public List<Proyecto> obtenerProyectos(List<EntidadProyecto> all) {
        List<Proyecto> lista = new ArrayList<Proyecto>();
        for (int i = 0; i < all.size(); i++){
            lista.add(obtenerProyecto(all.get(i)));
        }
        return lista;
    }

    public EntidadProyecto obtenerEntidad(Proyecto proyecto) {
        return proyecto.obtenerEntidad();
    }
}
