package persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProyectosRepository extends JpaRepository<EntidadProyecto, Long> {

   /* public Map<Long,Proyecto> findAll() {
        return proyectos;
    }

    public Proyecto obtenerProyecto(long id){
        return proyectos.get(id);
    }

    public int obtenerCantidadDeProyectos(){
        return proyectos.size();
    }

    public Proyecto agregarProyecto(Proyecto proyecto) {
        proyectos.put(proyecto.getId(), proyecto);
        return proyectos.get(proyecto.getId());
    }

    public Proyecto modificar(long id, String nombre) {
        Proyecto aux = proyectos.get(id);
        if(aux != null){
            aux.setNombre(nombre);
        } else {
            proyectos.put(id, new ProyectoDeImplementacion(id, nombre));
        }
        return proyectos.get(id);
    }

    public void borrar(long id) {
        proyectos.remove(id);
    }

    */
}
