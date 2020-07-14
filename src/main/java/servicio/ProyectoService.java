package servicio;

import excepciones.FaseNotFoundException;
import excepciones.ParametrosInvalidosException;
import excepciones.ProyectoNotFoundException;
import modelo.Fase;
import modelo.Proyecto;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import persistencia.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
public class ProyectoService {
    @Autowired
    private ProyectosRepository proyectosRepository;

    @Autowired
    private FasesRepository fasesRepository;

    private final Conversor conversor = new Conversor();

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
    public List<Proyecto> findAll(){
        return conversor.obtenerProyectos(proyectosRepository.findAll());
    }
    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
    public Proyecto save(Proyecto proyecto){
        EntidadProyecto entidad = proyectosRepository.save(conversor.obtenerEntidad(proyecto));
        return conversor.obtenerProyecto(entidad);
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
    public Proyecto saveNew(Proyecto proyecto){
        proyecto.setId(null);
        EntidadProyecto entidad = proyectosRepository.save(conversor.obtenerEntidad(proyecto));
        return conversor.obtenerProyecto(entidad);
    }
    public void delete(Proyecto proyecto){
        proyectosRepository.delete(conversor.obtenerEntidad(proyecto));
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
    public Proyecto getOne(long id) {
        if (!proyectosRepository.existsById(id)){
            throw new ProyectoNotFoundException("Proyecto con id: " + id + " no encontrado");
        }
        EntidadProyecto entidad = proyectosRepository.getOne(id);
        return conversor.obtenerProyecto(entidad);
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
        proyectosRepository.save(conversor.obtenerEntidad(proyecto));
    }

    public Fase crearFase(Long proyectoId, Fase fase) {
        Proyecto proyecto = getOne(proyectoId);
        proyecto.crearFase(fase);
        //List <Fase> fases = proyecto.obtenerFases();
        //EntidadFase entidadFase =  fasesRepository.save(conversor.obtenerEntidad(fases.get(fases.size()-1)));
        proyecto = save(proyecto);
        //EntidadProyecto entidadProyecto = proyectosRepository.save(conversor.obtenerEntidad(proyecto));
        //List <EntidadFase> fases = entidadProyecto.getFases();
        //return new Fase(fasesRepository.getOne(fases.get(fases.size()-1).getId()));
        return proyecto.obtenerFases().get(proyecto.obtenerFases().size()-1);
    }

    public Fase obtenerFase(Long proyectoId, Long faseId) {
        Proyecto proyecto = getOne(proyectoId);
        if (proyecto.existeFase(faseId)){
            return proyecto.obtenerFase(faseId);
        }else {
            throw new FaseNotFoundException("Fase con id " + faseId + " no encontrada");
        }
    }

    public List<Fase> obtenerFases(Long proyectoId) {
        Proyecto proyecto = getOne(proyectoId);
        return proyecto.obtenerFases();
    }

    public void borrarFase(Long proyectoId, Long faseId) {
        Proyecto proyecto = getOne(proyectoId);
        if (proyecto.existeFase(faseId)){
            proyecto.borrarFase(faseId);
            save(proyecto);
            fasesRepository.deleteById(faseId);
        }else {
            throw new FaseNotFoundException("Fase con id " + faseId + " no encontrada");
        }

    }
    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
    public Fase guardarFase(Long proyectoId, Long faseId,Fase fase) {
        Proyecto proyecto = getOne(proyectoId);
        //if (proyecto.existeFase(faseId)){
            fase.setId(faseId);
            proyecto.guardarFase(fase);
            proyecto = save(proyecto);
            List<Fase> fasesGuardadas = proyecto.obtenerFases();
            return fasesGuardadas.get(fasesGuardadas.size()-1);
        //} else{
        //    return crearFase(proyectoId, fase);
        //}
    }
}
