package servicio;

import excepciones.FechaInvalidaException;
import excepciones.ParametrosInvalidosException;
import excepciones.ProyectoNotFoundException;
import modelo.Tarea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import modelo.Fase;
import persistencia.FasesRepository;
import modelo.Proyecto;
import persistencia.ProyectosRepository;
import persistencia.TareasRepository;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Service
public class ProyectoService {
    @Autowired
    private ProyectosRepository proyectosRepository;

    @Autowired
    private FasesRepository fasesRepository;
    @Autowired
    private TareasRepository tareasRepository;

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
}
