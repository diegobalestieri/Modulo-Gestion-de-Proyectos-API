package servicio;

import excepciones.*;
import modelo.Iteracion;
import modelo.Tarea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import modelo.Fase;
import persistencia.FasesRepository;
import modelo.Proyecto;
import persistencia.IteracionesRepository;
import persistencia.ProyectosRepository;
import persistencia.TareasRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Service
public class ProyectoService {
    @Autowired
    private ProyectosRepository proyectosRepository;

    @Autowired
    private FasesRepository fasesRepository;
    @Autowired
    private TareasRepository tareasRepository;
    @Autowired
    private IteracionesRepository iteracionesRepository;

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
    public List<Proyecto> findAll(){
        return proyectosRepository.findAll();
    }
    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
    public Proyecto save(Proyecto proyecto){
        return proyectosRepository.save(proyecto);
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
    public Proyecto saveNew(Proyecto proyecto){
        proyecto.setId(null);
        return proyectosRepository.save(proyecto);
    }
    public void delete(Proyecto proyecto){
        proyectosRepository.delete(proyecto);
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
    public Proyecto getOne(long id) {
        if (!proyectosRepository.existsById(id)){
            throw new ProyectoNotFoundException("El proyecto no fue encontrado");
        }
        return proyectosRepository.getOne(id);
    }

    public void deleteById(long id) {
        proyectosRepository.deleteById(id);
    }

    public void deleteAll() {
        proyectosRepository.deleteAll();
    }

    public void update(Proyecto proyecto, Map<String, Object> parametros) {
        try{
            proyecto.actualizar(parametros);
        } catch (ParseException e){
            throw new ParametrosInvalidosException(e.getMessage());
        }
        proyectosRepository.save(proyecto);
    }

    public Fase crearFase(Long proyectoId, Fase fase) throws FechaInvalidaException, ParseException {
        Proyecto proyecto = getOne(proyectoId);
        proyecto.crearFase(fase);
        Proyecto proyectoGuardado = proyectosRepository.save(proyecto);
        List <Fase> fases = proyectoGuardado.getFases();
        return fases.get(fases.size()-1);
    }

    public Fase obtenerFase(Long proyectoId, Long faseId) {
        Proyecto proyecto = getOne(proyectoId);
        return proyecto.obtenerFase(faseId);
    }

   public List<Fase> obtenerFases(Long proyectoId) {
        Proyecto proyecto = getOne(proyectoId);
        return proyecto.obtenerFases();
    }

    public void borrarFase(Long proyectoId, Long faseId) {
        Proyecto proyecto = getOne(proyectoId);
        proyecto.borrarFase(faseId);
        save(proyecto);
        fasesRepository.deleteById(faseId);
    }

    public Fase guardarFase(Long proyectoId, Long faseId,Fase fase) {
        Proyecto proyecto = getOne(proyectoId);
        fase.setId(faseId);
        proyecto.guardarFase(fase);
        Proyecto entidadProyecto = proyectosRepository.save(proyecto);
        List <Fase> fases = entidadProyecto.getFases();
        return fases.get(fases.size()-1);
    }

    public Tarea crearTarea(Long proyectoId, Tarea tarea) {
        Proyecto proyecto = getOne(proyectoId);
        proyecto.crearTarea(tarea);
        proyecto = proyectosRepository.save(proyecto);
        List<Tarea> tareas = proyecto.obtenerTareas();
        return tareas.get(tareas.size()-1);
    }

    public List<Tarea> obtenerTareas(Long proyectoId) {
        Proyecto proyecto = getOne(proyectoId);
        return proyecto.obtenerTareas();
    }

    public List<Tarea> obtenerTareasSinIteracion(Long proyectoId) {
        Proyecto proyecto = getOne(proyectoId);
        return proyecto.obtenerTareasSinIteracion();
    }

    public Tarea obtenerTarea(Long proyectoId, Long tareaId) {
        Proyecto proyecto = getOne(proyectoId);
        return proyecto.obtenerTarea(tareaId);
    }

    public Tarea guardarTarea(Long proyectoId, Long tareaId, Tarea tarea) {
        Proyecto proyecto = getOne(proyectoId);
        tarea.setId(tareaId);
        proyecto.guardarTarea(tarea);
        Proyecto entidadProyecto = proyectosRepository.save(proyecto);
        List <Tarea> tareas = entidadProyecto.getTareas();
        return tareas.get(tareas.size()-1);
    }

    public void borrarTarea(Long proyectoId, Long tareaId) {
        Proyecto proyecto = getOne(proyectoId);
        proyecto.borrarTarea(tareaId);
        save(proyecto);
        tareasRepository.deleteById(tareaId);
    }

    public List<Iteracion> obtenerIteraciones(Long proyectoId, Long faseId) {
        Proyecto proyecto = getOne(proyectoId);
        Fase fase = proyecto.obtenerFase(faseId);
        return fase.obtenerIteraciones();
    }

    public Iteracion obtenerIteracion(Long proyectoId, Long faseId, Long iteracionId) {
        Proyecto proyecto = getOne(proyectoId);
        Fase fase = proyecto.obtenerFase(faseId);
        return fase.obtenerIteracion(iteracionId);
    }

    public Iteracion crearIteracion(Long proyectoId, Long faseId,Iteracion iteracion) {
        Proyecto proyecto = getOne(proyectoId);
        Fase fase = proyecto.obtenerFase(faseId);
        List <Iteracion> iteraciones = fase.obtenerIteraciones();
        fase.agregarIteracion(iteracion);
        proyecto = proyectosRepository.save(proyecto);
        return iteraciones.get(iteraciones.size()-1);
    }

    public Iteracion guardarIteracion(Long proyectoId, Long faseId, Long iteracionId, Iteracion iteracion) {
        Proyecto proyecto = getOne(proyectoId);
        Fase fase = proyecto.obtenerFase(faseId);
        iteracion.setId(iteracionId);
        fase.guardarIteracion(iteracion);
        Proyecto entidadProyecto = proyectosRepository.save(proyecto);
        List <Iteracion> iteraciones = entidadProyecto.obtenerFase(faseId).obtenerIteraciones();
        return iteraciones.get(iteraciones.size()-1);
    }

    public void borrarIteracion(Long proyectoId, Long faseId,Long iteracionId) throws AccionNoPermitidaException {
        Proyecto proyecto = getOne(proyectoId);
        Fase fase = proyecto.obtenerFase(faseId);
        fase.eliminarIteracion(iteracionId);
        save(proyecto);
        iteracionesRepository.deleteById(iteracionId);
    }

    public List<Tarea> obtenerTareasDeIteracion(Long proyectoId, Long faseId, Long iteracionId) {
        Proyecto proyecto = getOne(proyectoId);
        Fase fase = proyecto.obtenerFase(faseId);
        Iteracion iteracion = fase.obtenerIteracion(iteracionId);
        return iteracion.obtenerTareasDeIteracion(proyecto.obtenerTareas());
    }

    public Iteracion agregarTareaAIteracion(Long proyectoId, Long faseId, Long iteracionId, Long tareaId) {
        Tarea tarea = getOne(proyectoId).obtenerTarea(tareaId);
        if (tarea.getIteracion() != 0) {
            throw new AccionNoPermitidaException("La tarea ya está asignada a una iteración");
        }
        Proyecto proyecto = getOne(proyectoId);
        Fase fase = proyecto.obtenerFase(faseId);
        Iteracion iteracion = fase.obtenerIteracion(iteracionId);
        iteracion.agregarTarea(tareaId);
        tarea.setIteracion(iteracionId);
        Proyecto entidadProyecto = proyectosRepository.save(proyecto);
        List <Iteracion> iteraciones = entidadProyecto.obtenerFase(faseId).obtenerIteraciones();
        return iteraciones.get(iteraciones.size()-1);
    }

    public void borrarTareaDeIteracion(Long proyectoId, Long faseId,Long iteracionId, Long tareaId) {
        Proyecto proyecto = getOne(proyectoId);
        Fase fase = proyecto.obtenerFase(faseId);
        Iteracion iteracion = fase.obtenerIteracion(iteracionId);
        iteracion.eliminarTarea(tareaId);
        proyecto.obtenerTarea(tareaId).setIteracion(0);
        save(proyecto);

    }

    public void finalizarIteracion(Long proyectoId, Long faseId,Long iteracionId) {
        Proyecto proyecto = getOne(proyectoId);
        Fase fase = proyecto.obtenerFase(faseId);
        Iteracion iteracion = fase.obtenerIteracion(iteracionId);
        iteracion.finalizar();
        save(proyecto);
    }


}
